package land.kho.meta.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val TAG = "dataStore"

/**
 * datastore instance
 */

private val Context.myDataStore by preferencesDataStore(name = PreferenceManager.PREFERENCE_NAME)

/**
 * Interface to manage the data in preference data
 */

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {

        const val PREFERENCE_NAME = "kho-land-preference"
        val PRE_IS_WALLET_CREATED = booleanPreferencesKey("is_wallet_created")
        val PRE_TOKEN = stringPreferencesKey("token")
        val PRE_MNEMONIC = stringPreferencesKey("mnemonic")
        val PRE_ACCOUNT_ADDRESS = stringPreferencesKey("account_address")
        val PRE_IS_MNEMONIC_SAVED= booleanPreferencesKey("is_mnemonic_saved")
        val PRE_EMAIL= stringPreferencesKey("email")
        val PRE_USER_ID= intPreferencesKey("user_id")
        val PRE_IS_VERIFIED= booleanPreferencesKey("is_verified")
        val PRE_TEMP_EMAIL= stringPreferencesKey("temp_email")
        val PRE_USER_NAME = stringPreferencesKey("user_name")
        val PRE_REFERRAL_CODE = stringPreferencesKey("referral_code")
        val PRE_IS_REFERRAL_COMPLETED= booleanPreferencesKey("is_referral_completed")
        val PRE_REFERRED_BY = stringPreferencesKey("referred_by")
        val PRE_NUMBER = stringPreferencesKey("number")
        val PRE_IS_NEW_FCM_TOKEN_SYNCED= booleanPreferencesKey("is_new_fcm_token_synced")

    }

    /**
     * datastore instance
     */

    private val dataStore: DataStore<Preferences> = context.myDataStore

    /**
     * get preference when required
     */

    suspend fun <T> getValue(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        dataStore.data.catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }.map { preferences ->
            Utility.log(tag = TAG, msg = "returned ${key.name} : ${preferences[key]} ")
            preferences[key] ?: defaultValue
        }


    /**
     * write preference when required
     */

    suspend fun <T> putValue(
        key: Preferences.Key<T>,
        value: T
    ) {
        dataStore.edit {
            Utility.log(tag = TAG, msg = "added ${key.name} : $value")
            it[key] = value
        }
    }

    /**
     * clear preference except fCM
     */

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

}