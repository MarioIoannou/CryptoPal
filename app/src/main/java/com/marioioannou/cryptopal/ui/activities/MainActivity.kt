package com.marioioannou.cryptopal.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
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
import com.marioioannou.cryptopal.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var networkListener: NetworkListener

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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