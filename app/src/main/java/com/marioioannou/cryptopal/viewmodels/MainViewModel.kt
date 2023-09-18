package com.marioioannou.cryptopal.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.marioioannou.cryptopal.domain.database.CryptoCoinEntity
import com.marioioannou.cryptopal.domain.datastore.DataStoreRepository
import com.marioioannou.cryptopal.domain.datastore.DatastoreRepo
import com.marioioannou.cryptopal.domain.model.coins.CoinInfo
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import com.marioioannou.cryptopal.domain.repository.Repository
import com.marioioannou.cryptopal.utils.Constants
import com.marioioannou.cryptopal.utils.Constants.DEFAULT_CURRENCY
import com.marioioannou.cryptopal.utils.Constants.DEFAULT_FILTER
import com.marioioannou.cryptopal.utils.Constants.PREFERENCES_CURRENCY
import com.marioioannou.cryptopal.utils.Constants.PREFERENCES_FILTER
import com.marioioannou.cryptopal.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.Response
import java.util.Currency
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application,
    private val dataStoreRepo: DatastoreRepo,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var networkStatus = false
    var backOnline = false
    var savedCoin = false

    /* DATASTORE */ /* ------------------------------------------------------------------------- */
    fun saveCurrency(symbol: String){
        Log.e(TAG,"Pref -> $symbol")
        runBlocking {
            dataStoreRepo.putCurrency(PREFERENCES_CURRENCY, symbol)
        }
    }

    fun getCurrency():String = runBlocking {
        //Log.e(TAG,"Pref before getCurrency -> ${dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)}")
        if (dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)== null){
            return@runBlocking DEFAULT_CURRENCY
        }else{
            //Log.e(TAG,"Pref after getCurrency -> ${dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)!!}")
            dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)!!
        }
    }

    fun saveFilter(filter: String){
        Log.e(TAG,"Pref -> $filter")
        runBlocking {
            dataStoreRepo.putCurrency(PREFERENCES_FILTER, filter)
        }
    }

    fun getFilter():String = runBlocking {
        Log.e(TAG,"Pref before getFilter -> ${dataStoreRepo.getFilter(PREFERENCES_FILTER)}")
        if (dataStoreRepo.getFilter(PREFERENCES_FILTER)== null){
            return@runBlocking DEFAULT_FILTER
        }else{
            Log.e(TAG,"Pref after getFilter -> ${dataStoreRepo.getFilter(PREFERENCES_FILTER)!!}")
            dataStoreRepo.getFilter(PREFERENCES_CURRENCY)!!
        }
    }

    /* ROOM DATABASE */ /* --------------------------------------------------------------------- */
    val readCryptoCoin: LiveData<List<CryptoCoinEntity>> =
        repository.local.readCryptoCoins().asLiveData()

    //val watchlistData = MutableLiveData<>

    fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity) =
        viewModelScope.launch{
            repository.local.insertCryptoCoin(cryptoCoinEntity)
        }

    fun updateSpecificCoins() = viewModelScope.launch {
        repository.local.updateCryptoCoinsData()
    }

    fun deleteCryptoCoin(cryptoCoinEntity: CryptoCoinEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteCryptoCoin(cryptoCoinEntity)
        }

    fun deleteAllCryptoCoins() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllCryptoCoins()
    }

    /* RETROFIT */ /* -------------------------------------------------------------------------- */

    private val _coinResponse: MutableLiveData<ScreenState<CryptoCoins>> = MutableLiveData()
    val coinResponse = _coinResponse

    private val _coinInfoResponse: MutableLiveData<ScreenState<CoinInfo>> = MutableLiveData()
    val coinInfoResponse = _coinInfoResponse

    private val _coinMarketChartResponse: MutableLiveData<ScreenState<CoinMarketChart>> =
        MutableLiveData()
    val coinMarketChartResponse = _coinMarketChartResponse

    private val _searchCoinResponse: MutableLiveData<ScreenState<SearchCoin>> = MutableLiveData()
    val searchCoinResponse = _searchCoinResponse

    private val _newsResponse: MutableLiveData<ScreenState<CryptoNews>> = MutableLiveData()
    val newsResponse = _newsResponse

    private var TAG = "MainViewModel"

    fun getCoins(queries: Map<String, String>) = viewModelScope.launch {
        getCoinsConnected(queries)
    }

    fun getCoinInfo(coin_id: String, currency: String) = viewModelScope.launch {
        getCoinsInfoConnected(coin_id,currency)
    }

    fun getCryptoCoinMarketChart(id: String, currency: String, days: String) =
        viewModelScope.launch {
            getCryptoCoinMarketChartConnected(id, currency, days)
        }

    fun getSearchedCoin(query: String) = viewModelScope.launch {
        getSearchedCoinConnected(query)
    }

    fun getNews(id: String, queries: Map<String, String>) = viewModelScope.launch {
        getNewsConnected(id,queries)
    }

    // Connected
    private suspend fun getCoinsConnected(queries: Map<String, String>) {
        coinResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                Log.e(TAG, "getCoinsConnected() hasInternetConnection()")
                val response = repository.remote.getCoins(queries)
                Log.e(TAG, "getCoinsConnected() .remote.getCoins(queries)")
                coinResponse.postValue(handleCryptoCoinsResponse(response))
                Log.e(TAG, "getCoinsConnected() handleCryptoCoinsResponse()")
            } else {
                coinResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> coinResponse.postValue(ScreenState.Error(null, "Network Failure"))
                else -> coinResponse.postValue(ScreenState.Error(null, "Something went wrong"))
            }
        }
    }

    private suspend fun getCoinsInfoConnected(coin_id: String, currency: String) {
        coinInfoResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                Log.e(TAG, "getCoinsConnected() hasInternetConnection()")
                val response = repository.remote.getCoinInfo(coin_id, currency)
                Log.e(TAG, "getCoinsConnected() .remote.getCoins(queries)")
                coinInfoResponse.postValue(handleCryptoCoinInfoResponse(response))
                Log.e(TAG, "getCoinsConnected() handleCryptoCoinsResponse()")
            } else {
                coinInfoResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> coinInfoResponse.postValue(ScreenState.Error(null, "Network Failure"))
                else -> coinInfoResponse.postValue(ScreenState.Error(null, "Something went wrong"))
            }
        }
    }

    private suspend fun getCryptoCoinMarketChartConnected(
        id: String,
        currency: String,
        days: String,
    ) {
        coinMarketChartResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getCryptoCoinMarketChart(id, currency, days)
                coinMarketChartResponse.postValue(handleCryptoCoinMarketChartResponse(response))
            } else {
                coinMarketChartResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> coinMarketChartResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> coinMarketChartResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getSearchedCoinConnected(query: String) {
        searchCoinResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getSearchedCoin(query)
                searchCoinResponse.postValue(handleSearchedCoinResponse(response))
            } else {
                searchCoinResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchCoinResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> searchCoinResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    private suspend fun getNewsConnected(id: String, queries: Map<String, String>) {
        newsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getNews(id,queries)
                newsResponse.postValue(handleNewsResponse(response))
            } else {
                newsResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsResponse.postValue(ScreenState.Error(null,
                    "Network Failure"))
                else -> newsResponse.postValue(ScreenState.Error(null,
                    "Something went wrong"))
            }
        }
    }

    // Handle Responses //
    private fun handleCryptoCoinsResponse(response: Response<CryptoCoins>): ScreenState<CryptoCoins>? {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Coin not found")
            }
            response.isSuccessful -> {
                val coinResponse = response.body()
                return ScreenState.Success(coinResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleCryptoCoinInfoResponse(response: Response<CoinInfo>): ScreenState<CoinInfo>? {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Coin not found")
            }
            response.isSuccessful -> {
                val coinResponse = response.body()
                return ScreenState.Success(coinResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleCryptoCoinMarketChartResponse(response: Response<CoinMarketChart>): ScreenState<CoinMarketChart> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Coin Market Chart not found")
            }
            response.isSuccessful -> {
                val cryptoCoinMarketChartResponse = response.body()
                return ScreenState.Success(cryptoCoinMarketChartResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleSearchedCoinResponse(response: Response<SearchCoin>): ScreenState<SearchCoin> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Searched Coin not found")
            }
            response.isSuccessful -> {
                val searchedCoinResponse = response.body()
                return ScreenState.Success(searchedCoinResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleNewsResponse(response: Response<CryptoNews>): ScreenState<CryptoNews> {
        when {
            response.message().toString().contains("timeout") -> {
                Log.d("Error1", response.message().toString())
                return ScreenState.Error(null, "Timeout")
            }
            response.code() == 402 -> {
                Log.d("Error2", response.code().toString())
                return ScreenState.Error(null, "API Key Limit Achieved.")
            }
            response.body() == null -> {
                Log.d("Error3", response.message().toString())
                return ScreenState.Error(null, "Searched Coin not found")
            }
            response.isSuccessful -> {
                val newsResponse = response.body()
                return ScreenState.Success(newsResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    /* QUERIES */
    fun applyCoinsQueries(): HashMap<String, String> {
        Log.e(TAG, "Currency -> ${getCurrency()}")
        val queries: HashMap<String, String> = HashMap()
        queries["limit"] = "500"
        //queries["price_change_percentage"] = "1h,24h,7d,14d,30d,200d,1y"
        //queries["sparkline"] = "true"
//        if (this@MainViewModel::currency.isInitialized) {
//            queries["vs_currency"] = currency
//        }else{
//            queries["vs_currency"] = Constants.DEFAULT_CURRENCY
//        }
        //queries["vs_currency"] = getCurrency()
        queries["currency"] = getCurrency()
        return queries
    }

    /* INTERNET */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}