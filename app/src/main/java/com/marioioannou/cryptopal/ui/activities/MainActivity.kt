package com.marioioannou.cryptopal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.marioioannou.cryptopal.R
import com.marioioannou.cryptopal.databinding.ActivityMainBinding
import com.marioioannou.cryptopal.utils.NetworkListener
import com.marioioannou.cryptopal.ui.viewmodels.MainViewModel
import com.marioioannou.cryptopal.utils.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var networkListener: NetworkListener

    private var TAG = "MainActivity"

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestApiData()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(this@MainActivity)
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    viewModel.networkStatus = status
                    viewModel.showNetworkStatus()
                    if (!status){
                        //showNoInternetLayout()
                        disconnected()
                    }else{
                        connected()
                        requestApiData()
                    }
                }
        }

        binding.btnWifiSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        setSupportActionBar(binding.toolbar)
        hideToolbar()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.marketFragment,
                R.id.newsFragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    showBottomNav()
                    //hideToolbar()
                }
                R.id.marketFragment -> {
                    showBottomNav()
                    //showToolbar()
                }
                R.id.newsFragment -> {
                    showBottomNav()
                    //showToolbar()
                }
//                R.id.triviaFragment -> {
//                    showBottomNav()
//                    showToolbar()
//                }
//                R.id.recipesBottomSheet -> {
//                    showBottomNav()
//                    showToolbar()
//                }
//                R.id.searchIngredientsFragment -> {
//                    hideBottomNav()
//                    hideToolbar()
//                }
                else -> {
                    hideBottomNav()
                    //showToolbar()
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)
    }

//    fun checkThemeMode(){
//        binding.apply {
//            viewModel.readTheme.observe(this@MainActivity){mode->
//                when(mode.lowercase()){
//                    "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                    "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                    "system default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//                }
//            }
//        }
//    }

    private fun requestApiData() {
        Log.e(TAG, "requestApiData CALLED")
        viewModel.getCoins(viewModel.applyCoinsQueries())
        viewModel.coinResponse.observe(this, Observer { recipeResponse ->
            Log.e(TAG, "viewModel.requestApiData.observe")
            when (recipeResponse) {
                is ScreenState.Loading -> {
                    Log.d(TAG, "   requestApiData() Response Loading")
                }
                is ScreenState.Success -> {
                    Log.d(TAG, "   requestApiData() Response Success")
                }
                is ScreenState.Error -> {
                    Log.d(TAG, "   requestApiData() Response Error")
                    Toast.makeText(
                        this,
                        recipeResponse.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
//                    if (recipeResponse.data?.results.isNullOrEmpty()){
//                        Log.e(TAG, "is Empty()")
//                        binding.shimmerFragRecipesRv.visibility = View.GONE
//                        binding.rvRecipes.visibility = View.INVISIBLE
//                        binding.noInternetLayout.visibility = View.VISIBLE
//                    }else{
//                        loadCachedData()
//                    }
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
    }

    private fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun disconnected() {
        binding.bottomNavigationView.visibility = View.GONE
        binding.navHostFragment.visibility = View.GONE
        binding.layoutNoInternet.visibility = View.VISIBLE
    }

    private fun connected() {
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.navHostFragment.visibility = View.VISIBLE
        binding.layoutNoInternet.visibility = View.GONE
    }
}