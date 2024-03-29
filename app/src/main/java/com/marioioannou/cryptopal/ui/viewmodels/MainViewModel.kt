package com.marioioannou.cryptopal.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.*
import com.marioioannou.cryptopal.domain.database.crypto_coins.CryptoCoinEntity
import com.marioioannou.cryptopal.data.datastore.ProtoRepository
import com.marioioannou.cryptopal.domain.database.crypto_watchlist.CryptoWatchlistEntity
import com.marioioannou.cryptopal.domain.model.coins.CryptoCoins
import com.marioioannou.cryptopal.domain.model.coins.Result
import com.marioioannou.cryptopal.domain.model.market_chart.CoinMarketChart
import com.marioioannou.cryptopal.domain.model.news.CryptoNews
import com.marioioannou.cryptopal.domain.model.search_coins.SearchCoin
import com.marioioannou.cryptopal.domain.repository.Repository
import com.marioioannou.cryptopal.utils.Constants
import com.marioioannou.cryptopal.utils.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application,
    private val datastore: ProtoRepository,
    //private val dataStoreRepo: DatastoreRepo,
    //private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    var networkStatus = false
    var backOnline = false
    var savedCoin = false

    var times = 1

    // - Retrofit - //
    private val _coinResponse: MutableLiveData<ScreenState<CryptoCoins>> = MutableLiveData()
    val coinResponse = _coinResponse

    private val _coinInfoResponse: MutableLiveData<ScreenState<Result>> = MutableLiveData()
    val coinInfoResponse = _coinInfoResponse

    private val _coinMarketChartResponse: MutableLiveData<ScreenState<CoinMarketChart>> =
        MutableLiveData()
    val coinMarketChartResponse = _coinMarketChartResponse

    private val _searchCoinResponse: MutableLiveData<ScreenState<SearchCoin>> = MutableLiveData()
    val searchCoinResponse = _searchCoinResponse

    private val _newsResponse: MutableLiveData<ScreenState<CryptoNews>> = MutableLiveData()
    val newsResponse = _newsResponse

//    private val _cryptoCoinInfo: MutableLiveData<ScreenState<CryptoCoinEntity>> = MutableLiveData()
//    val cryptoCoinInfo = _cryptoCoinInfo

    private var TAG = "MainViewModel"


    // - Database - //
    val readCoins: LiveData<List<CryptoCoinEntity>> =
        repository.local.readCryptoCoins().asLiveData()

    val readWatchlist: LiveData<List<CryptoWatchlistEntity>> =
        repository.local.readCryptoWatchlist().asLiveData()

    private val _watchlistStatus: MutableLiveData<Boolean> = MutableLiveData()
        val watchlistStatus = _watchlistStatus

    val readBackOnline = datastore.readBackOnline.asLiveData()
    val readCurrency = datastore.readCurrency.asLiveData()
    val readThemeMode = datastore.readThemeMode.asLiveData()

    /* DATASTORE */ /* ------------------------------------------------------------------------- */
    fun saveCurrency(symbol: String) {
        Log.e(TAG, "Pref -> $symbol")
        runBlocking {
            //dataStoreRepo.putCurrency(PREFERENCES_CURRENCY, symbol)
            datastore.saveCurrency(symbol)
        }
    }

//    val readCurrency = viewModelScope.launch {
//        datastore.readCurrency.collect { currentCurrency ->
//         _readCurrency.value = currentCurrency}
//    }

//    fun getCurrency():String = runBlocking {
//        //Log.e(TAG,"Pref before getCurrency -> ${dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)}")
//        if (dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)== null){
//            return@runBlocking DEFAULT_CURRENCY
//        }else{
//            //Log.e(TAG,"Pref after getCurrency -> ${dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)!!}")
//            dataStoreRepo.getCurrency(PREFERENCES_CURRENCY)!!
//        }
//    }

//    fun saveFilter(filter: String){
//        Log.e(TAG,"Pref -> $filter")
//        runBlocking {
//            dataStoreRepo.putCurrency(PREFERENCES_FILTER, filter)
//        }
//    }

//    fun getFilter():String = runBlocking {
//        Log.e(TAG,"Pref before getFilter -> ${dataStoreRepo.getFilter(PREFERENCES_FILTER)}")
//        if (dataStoreRepo.getFilter(PREFERENCES_FILTER)== null){
//            return@runBlocking DEFAULT_FILTER
//        }else{
//            Log.e(TAG,"Pref after getFilter -> ${dataStoreRepo.getFilter(PREFERENCES_FILTER)!!}")
//            dataStoreRepo.getFilter(PREFERENCES_CURRENCY)!!
//        }
//    }

    /* ROOM DATABASE */ /* --------------------------------------------------------------------- */


    val readCryptoCoin: LiveData<List<CryptoCoinEntity>> =
        repository.local.readCryptoCoins().asLiveData()

    //val watchlistData = MutableLiveData<>

    private fun insertCryptoCoin(cryptoCoinEntity: CryptoCoinEntity) =
        viewModelScope.launch {
            repository.local.insertCryptoCoin(cryptoCoinEntity)
        }

    fun insertWatchlistCryptoCoin(cryptoWatchlistEntity: CryptoWatchlistEntity) =
        viewModelScope.launch {
            repository.local.insertCryptoWatchlist(cryptoWatchlistEntity)
        }

    fun updateSpecificCoins() = viewModelScope.launch {
        repository.local.updateCryptoCoinsData()
    }

    fun getCryptoCoinInfo(id: String) = viewModelScope.launch {
        coinInfoResponse.value = ScreenState.Loading()
        try {
            val localCoinData = repository.local.getCryptoCoinFromDatabase(id)
            if (localCoinData != null){
                coinInfoResponse.value = ScreenState.Success(localCoinData.cryptoCoin.result.first())
            }else{
                val response = repository.remote.getCoinInfo(id,currentCurrency())
                if (response.isSuccessful && response.body() != null) {
                    coinInfoResponse.value = ScreenState.Success(response.body())
                } else {
                    coinInfoResponse.value = ScreenState.Error(message = "Network error: ${response.errorBody()?.string() ?: "Unknown error"}")
                }
            }
        } catch (e: Exception) {
            coinInfoResponse.value = ScreenState.Error(message = "Exception: ${e.message}")
        }
    }

    fun deleteCryptoCoin(cryptoCoinEntity: CryptoWatchlistEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteCryptoWatchlist(cryptoCoinEntity)
        }

    fun deleteAllCryptoCoins() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllCryptoWatchlist()
    }

    fun isWatchlistEmpty()= viewModelScope.launch(Dispatchers.IO) {
        repository.local.isWatchlistEmpty()
    }


    /* RETROFIT */ /* -------------------------------------------------------------------------- */

    fun getCoins(queries: Map<String, String>) = viewModelScope.launch {
        getCoinsConnected(queries)
    }

    fun getCoinInfo(coin_id: String, currency: String) = viewModelScope.launch {
        getCoinsInfoConnected(coin_id, currency)
    }

    fun getCryptoCoinMarketChart(id: String, currency: String, days: String) =
        viewModelScope.launch {
            getCryptoCoinMarketChartConnected(id, currency, days)
        }

    fun getSearchedCoin(query: String) = viewModelScope.launch {
        getSearchedCoinConnected(query)
    }

    fun getNews(id: String, queries: Map<String, String>) = viewModelScope.launch {
        getNewsConnected(id, queries)
    }

    // Connected
    private suspend fun getCoinsConnected(queries: Map<String, String>) {
        coinResponse.postValue(ScreenState.Loading())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (hasInternetConnection()) {
                    val response = repository.remote.getCoins(queries)
                    withContext(Dispatchers.Main) {
                        coinResponse.value = handleCryptoCoinsResponse(response)
                        coinResponse.value?.data?.let { cryptoCoin ->
                            offlineCacheCryptoCoins(cryptoCoin)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        coinResponse.value = ScreenState.Error(null, "No Internet Connection")
                    }
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    coinResponse.value = ScreenState.Error(null, "Network Failure")
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    coinResponse.value = ScreenState.Error(null, "Server Error")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    coinResponse.value = ScreenState.Error(null, "Unexpected Error")
                }
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
                is IOException -> coinInfoResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Network Failure"
                    )
                )
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
                is IOException -> coinMarketChartResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Network Failure"
                    )
                )
                else -> coinMarketChartResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Something went wrong"
                    )
                )
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
                is IOException -> searchCoinResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Network Failure"
                    )
                )
                else -> searchCoinResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Something went wrong"
                    )
                )
            }
        }
    }

    private suspend fun getNewsConnected(id: String, queries: Map<String, String>) {
        newsResponse.postValue(ScreenState.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.remote.getNews(id, queries)
                newsResponse.postValue(handleNewsResponse(response))
            } else {
                newsResponse.postValue(ScreenState.Error(null, "No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Network Failure"
                    )
                )
                else -> newsResponse.postValue(
                    ScreenState.Error(
                        null,
                        "Something went wrong"
                    )
                )
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
                Log.d("IS SUCCESS", coinResponse.toString())
                return ScreenState.Success(coinResponse!!)
            }
            else -> {
                Log.d("Error4", response.message().toString())
                return ScreenState.Error(null, response.message().toString())
            }
        }
    }

    private fun handleCryptoCoinInfoResponse(response: Response<Result>): ScreenState<Result>? {
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

    // - Offline cache data - //

    private fun offlineCacheCryptoCoins(cryptoCoin: CryptoCoins) {
        val cryptoCoinEntity = CryptoCoinEntity(cryptoCoin)
        Log.e("offlineCacheCrypto", "insertCryptoCoin()")
        Log.e("offlineCacheCrypto", cryptoCoin.toString())
        insertCryptoCoin(cryptoCoinEntity)
    }

    /* QUERIES */
    fun applyCoinsQueries(): HashMap<String, String> {
        Log.e(TAG, "Currency ViewModel -> ${currentCurrency()}")
        val queries: HashMap<String, String> = HashMap()
        queries[Constants.QUERY_LIMIT] = "120"
        //queries["price_change_percentage"] = "1h,24h,7d,14d,30d,200d,1y"
        //queries["sparkline"] = "true"
//        if (this@MainViewModel::currency.isInitialized) {
//            queries["vs_currency"] = currency
//        }else{
//            queries["vs_currency"] = Constants.DEFAULT_CURRENCY
//        }
        //queries["vs_currency"] = getCurrency()
        queries[Constants.QUERY_CURRENCY] = currentCurrency()
        return queries
    }

    /* INTERNET */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
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
            datastore.saveBackOnline(backOnline)
        }

    fun currentCurrency(): String {
        return runBlocking {
            datastore.readCurrency.first()
        }
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            //Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                //Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}