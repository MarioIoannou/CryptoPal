package com.marioioannou.cryptopal.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.marioioannou.cryptopal.UserPreferences
import com.marioioannou.cryptopal.utils.Constants
import com.marioioannou.cryptopal.utils.Constants.DEFAULT_CURRENCY
import com.marioioannou.cryptopal.utils.Constants.DEFAULT_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

//private val Context.dataStore: DataStore<UserPreferences> by dataStore(
//    fileName = Constants.PREFERENCES_NAME,
//    serializer = UserPreferencesSerializer
//)

class ProtoRepository @Inject constructor(
    private val userPreferencesStore: DataStore<UserPreferences>
) {

//    val readProto: Flow<UserPreferences> = userPreferencesStore.data
//        .catch { exception ->
//            if (exception is IOException){
//                Log.d("Error",exception.message.toString())
//                emit(UserPreferences.getDefaultInstance())
//            }else{
//                throw exception
//            }
//    }

    // - Currency - //

    val readCurrency: Flow<String> = userPreferencesStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            if (preferences.currency.isEmpty()) DEFAULT_CURRENCY else preferences.currency
        }

    suspend fun saveCurrency(currency: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder()
                .setCurrency(currency)
                .build()
        }
    }

    // - Theme Mode - //

    val readThemeMode: Flow<String> = userPreferencesStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            if (preferences.theme.isEmpty()) DEFAULT_THEME else preferences.theme
        }

    suspend fun saveThemeMode(currency: String) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder()
                .setTheme(currency)
                .build()
        }
    }

    // - Connection - //

    val readBackOnline: Flow<Boolean> = userPreferencesStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(UserPreferences.getDefaultInstance())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences.isBackOnline
        }

    suspend fun saveBackOnline(backOnline: Boolean) {
        userPreferencesStore.updateData { preferences ->
            preferences.toBuilder()
                .setIsBackOnline(backOnline)
                .build()
        }
    }

//    suspend fun updateUserPreferences(currency: String? = null, themeMode: String? = null) {
//        userPreferencesStore.updateData { preferences ->
//            preferences.toBuilder()
//                .setCurrency(currency ?: preferences.currency)
//                .setTheme(themeMode ?: preferences.theme)
//                .build()
//        }
//    }

}