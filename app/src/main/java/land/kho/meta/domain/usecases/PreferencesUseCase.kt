package land.kho.meta.domain.usecases

import kotlinx.coroutines.flow.Flow
import land.kho.meta.utils.PreferenceManager
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(
    private val preferenceManager: PreferenceManager
) {

    /** Read Token from DataStore */

    suspend fun getToken(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_TOKEN, "")

    /** Store Token in DataStore */

    suspend fun setToken(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_TOKEN, value)
    }

    /** Read  is wallet created from DataStore */

    suspend fun getIsWalletCreated(): Flow<Boolean?> =
        preferenceManager.getValue(PreferenceManager.PRE_IS_WALLET_CREATED, false)

    /** Store is wallet created in DataStore */

    suspend fun setIsWalletCreated(value: Boolean) {
        preferenceManager.putValue(PreferenceManager.PRE_IS_WALLET_CREATED, value)
    }


    /** Read  is Mnemonic Saved from DataStore */

    suspend fun getIsMnemonicSaved(): Flow<Boolean?> =
        preferenceManager.getValue(PreferenceManager.PRE_IS_MNEMONIC_SAVED, false)

    /** Store is Mnemonic Saved in DataStore */

    suspend fun setIsMnemonicSaved(value: Boolean) {
        preferenceManager.putValue(PreferenceManager.PRE_IS_MNEMONIC_SAVED, value)
    }


    /** Read mnemonic  from DataStore */

    suspend fun getMnemonic(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_MNEMONIC, "")

    /** Store mnemonic in DataStore */

    suspend fun setMnemonic(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_MNEMONIC, value)
    }

    /** Read account address  from DataStore */

    suspend fun getAccountAddress(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_ACCOUNT_ADDRESS, "")

    /** Store account address in DataStore */

    suspend fun setAccountAddress(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_ACCOUNT_ADDRESS, value)
    }


    /** Read email  from DataStore */

    suspend fun getEmail(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_EMAIL, "")

    /** Store email in DataStore */

    suspend fun setEmail(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_EMAIL, value)
    }


    /** Read temp email  from DataStore */

    suspend fun getTempEmail(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_TEMP_EMAIL, "")

    /** Store temp email in DataStore */

    suspend fun setTempEmail(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_TEMP_EMAIL, value)
    }


    /** Read user Id  from DataStore */

    suspend fun getUserId(): Flow<Int?> =
        preferenceManager.getValue(PreferenceManager.PRE_USER_ID, 0)

    /** Store user Id in DataStore */

    suspend fun setUserId(value: Int) {
        preferenceManager.putValue(PreferenceManager.PRE_USER_ID, value)
    }


    /** Read user Name  from DataStore */

    suspend fun getUserName(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_USER_NAME, "")

    /** Store user Name in DataStore */

    suspend fun setUserName(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_USER_NAME, value)
    }


    /** Read Referral Code from DataStore */

    suspend fun getReferralCode(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_REFERRAL_CODE, "")

    /** Store Referral Code in DataStore */

    suspend fun setReferralCode(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_REFERRAL_CODE, value)
    }


    /** Read Is Referral Completed from DataStore */

    suspend fun getIsReferralCompleted(): Flow<Boolean?> =
        preferenceManager.getValue(PreferenceManager.PRE_IS_REFERRAL_COMPLETED, false)

    /** Store Is Referral Completed in DataStore */

    suspend fun setIsReferralCompleted(value: Boolean) {
        preferenceManager.putValue(PreferenceManager.PRE_IS_REFERRAL_COMPLETED, value)
    }


    /** Read referred By from DataStore */

    suspend fun getReferredBy(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_REFERRED_BY, "")

    /** Store referred By in DataStore */

    suspend fun setReferredBy(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_REFERRED_BY, value)
    }

    /** Read number By from DataStore */

    suspend fun getNumber(): Flow<String?> =
        preferenceManager.getValue(PreferenceManager.PRE_NUMBER, "")

    /** Store number By in DataStore */

    suspend fun setNumber(value: String) {
        preferenceManager.putValue(PreferenceManager.PRE_NUMBER, value)
    }


    /** Read Is Verified from DataStore */

    suspend fun getIsVerified(): Flow<Boolean?> =
        preferenceManager.getValue(PreferenceManager.PRE_IS_VERIFIED, false)

    /** Store Is Verified in DataStore */

    suspend fun setIsVerified(value: Boolean) {
        preferenceManager.putValue(PreferenceManager.PRE_IS_VERIFIED, value)
    }

    /** Read Is Verified from DataStore */

    suspend fun isNewFcmTokenSynced(): Flow<Boolean?> =
        preferenceManager.getValue(PreferenceManager.PRE_IS_NEW_FCM_TOKEN_SYNCED, false)

    /** Store Is Verified in DataStore */

    suspend fun setIsNewFcmTokenSynced(value: Boolean) {
        preferenceManager.putValue(PreferenceManager.PRE_IS_NEW_FCM_TOKEN_SYNCED, value)
    }


    /**
     * clear preferences in DataStore
     */

    suspend fun clear() {
        preferenceManager.clear()
    }


}