//package com.marioioannou.cryptopal.data.datastore
//
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.*
//import androidx.datastore.preferences.preferencesDataStore
//import com.marioioannou.cryptopal.utils.Constants
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.android.scopes.ViewModelScoped
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.catch
//import kotlinx.coroutines.flow.map
//import okio.IOException
//import javax.inject.Inject
//
//private val Context.dataStore by preferencesDataStore(
//    name = Constants.PREFERENCES_NAME
//)
//
//@ViewModelScoped
//class DataStoreRepository @Inject constructor(
//    @ApplicationContext private val context: Context,
//) {
//    private object PreferenceKeys {
//        val selectedCurrency = stringPreferencesKey(Constants.PREFERENCES_CURRENCY)
//
//        val selectedFilter = stringPreferencesKey(Constants.PREFERENCES_FILTER)
//        val selectedFilterId = intPreferencesKey(Constants.PREFERENCES_FILTER_ID)
//
//        val selectedTheme = stringPreferencesKey(Constants.PREFERENCES_THEME)
//        val selectedThemeId = intPreferencesKey(Constants.PREFERENCES_THEME_ID)
//
       //val backOnline = booleanPreferencesKey(Constants.PREFERENCES_BACK_ONLINE)
//    }
//
//    private val dataStore: DataStore<Preferences> = context.dataStore
//
//    // CURRENCY //
//    suspend fun saveCurrency(currency: String) {
//        dataStore.edit { pref ->
//            pref[PreferenceKeys.selectedCurrency] = currency
//        }
//    }
//
//    val readCurrency : Flow<String> = dataStore.data.catch { exception ->
//        if (exception is IOException){
//            emit(emptyPreferences())
//        }else{
//            throw exception
//        }
//    }.map { pref ->
//        val selectedCurrency = pref[PreferenceKeys.selectedCurrency] ?: Constants.DEFAULT_CURRENCY
//        selectedCurrency
//    }
//
//    // FILTER //
//
//    // THEME //
//    suspend fun saveTheme(themeMode: String) {
//        dataStore.edit { pref ->
//            pref[PreferenceKeys.selectedTheme] = themeMode
//
//        }
//    }
//
//    val readTheme: Flow<String> = dataStore.data.catch { exception ->
//        if (exception is IOException) {
//            emit(emptyPreferences())
//        } else {
//            throw exception
//        }
//    }.map { pref ->
//        val selectedTheme = pref[PreferenceKeys.selectedTheme] ?: Constants.DEFAULT_THEME
//        selectedTheme
//    }
//
//    // BACK ONLINE //
//    suspend fun saveBackOnline(backOnline: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[PreferenceKeys.backOnline] = backOnline
//        }
//    }
//
//    val readBackOnline: Flow<Boolean> = dataStore.data
//        .catch { exception ->
//            if (exception is IOException) {
//                emit(emptyPreferences())
//            } else {
//                throw exception
//            }
//        }.map { preferences ->
//            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
//            backOnline
//        }
//
//    data class FilterPref(
//        val selectedFilter : String,
//        val selectedFilterId : Int
//    )
//
//    data class ThemePref(
//        val selectedTheme : String,
//        val selectedThemeId : Int
//    )
//}