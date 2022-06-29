package land.kho.meta.presentation.wallet.createWallet

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.R
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.GetAccessTokenRequest
import land.kho.meta.data.model.response.GetProfileDataResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.presentation.bsLoading.BsLoading
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import land.kho.meta.utils.Utility.getString
import land.kho.meta.utils.addPropertyChangedCallback
import land.kho.wallet.KhoWallet
import land.kho.wallet.KhoWalletAccount
import javax.inject.Inject

@HiltViewModel
class CreateWalletVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    val mnemonic = ObservableField("")
    var accountAddress = ""
    var phoneNumber = ""
    val mnemonicSaved = MutableLiveData<Event<Boolean>>()
    lateinit var loadingSheet: BsLoading
    var fcmToken = ""

    val createWallet = ObservableField(true).apply {
        addPropertyChangedCallback {
            if (get() == false) {
                if (mnemonic.get().isNullOrBlank()) {
                    getMnemonic()
                }
                if (accountAddress.isBlank()) {
                    getAccountAddress()
                }
            }
        }
    }

    init {
        getNumber()
    }

    fun saveAccount(khoWalletAccount: KhoWalletAccount) {
        viewModelScope.launch {
            preferencesUseCase.setIsWalletCreated(value = true)
            preferencesUseCase.setMnemonic(value = khoWalletAccount.mnemonic)
            preferencesUseCase.setAccountAddress(value = khoWalletAccount.accountAddress)
            withContext(Dispatchers.Main) {
                mnemonic.set(khoWalletAccount.mnemonic)
                accountAddress = khoWalletAccount.accountAddress
                createWallet.set(false)
            }
        }
    }

    fun signIn() {
        viewModelScope.launch {
            when (val response = repository.getNonce(
                publicAddress = accountAddress
            )) {
                is NetworkResponse.Success -> {
                    val signature = KhoWallet.signMessage(
                        message = "I am signing my one-time nonce: ${response.body?.nonce}",
                        mnemonic = mnemonic.get()!!
                    )
                    getToken(signature = signature)
                }
                is NetworkResponse.Error -> {
                    loadingSheet.loading(loading = false)
                    loadingSheet.showRetry(retry = true)
                    loadingSheet.message(msg = getString(R.string.no_internet_loading_error))
                }
            }
        }
    }

    private fun getNumber() {
        viewModelScope.launch {
            preferencesUseCase.getNumber().collect {
                if (!it.isNullOrBlank()) {
                    phoneNumber = it
                }
            }
        }
    }

    private fun getMnemonic() {
        viewModelScope.launch {
            preferencesUseCase.getMnemonic().collect {
                if (!it.isNullOrBlank()) {
                    withContext(Dispatchers.Main) {
                        mnemonic.set(it)
                    }
                }
            }
        }
    }

    private fun getAccountAddress() {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    accountAddress = it
                }
            }
        }
    }

    private fun getToken(signature: String) {
        viewModelScope.launch {
            when (val response = repository.getAccessToken(
                GetAccessTokenRequest(
                    signature = signature,
                    publicAddress = accountAddress,
                    phoneNumber = phoneNumber,
                    fcmToken = fcmToken
                )
            )) {
                is NetworkResponse.Success -> {
                    saveToken(token = response.body?.accessToken!!) {
                        getMe()
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                    loadingSheet.loading(loading = false)
                    loadingSheet.showRetry(retry = true)
                    loadingSheet.message(msg = response.message ?: "")
                }
            }
        }
    }

    private fun saveToken(
        token: String,
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setToken(value = token)
                isSaved.invoke(true)
            }
        }
    }

    private fun getMe() {
        viewModelScope.launch {
            when (val response = repository.getMe()) {
                is NetworkResponse.Success -> {
                    saveInDB(response = response.body!!) {
                        loadingSheet.dismissLoading()
                        mnemonicSaved.postValue(Event(content = true))
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                    loadingSheet.loading(loading = false)
                    loadingSheet.showRetry(retry = true)
                    loadingSheet.message(msg = response.message ?: "")
                }
            }
        }
    }

    private fun saveInDB(
        response: GetProfileDataResponse,
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setEmail(value = response.email ?: "")
                preferencesUseCase.setUserId(value = response.id)
                preferencesUseCase.setIsVerified(value = response.verified)
                preferencesUseCase.setUserName(value = response.nickname)
                preferencesUseCase.setIsMnemonicSaved(value = true)
                preferencesUseCase.setReferralCode(value = response.referralCode)
                preferencesUseCase.setIsReferralCompleted(value = response.isReferralCompleted)
                preferencesUseCase.setReferredBy(
                    value = response.referral?.referrerUser?.referralCode ?: ""
                )
                preferencesUseCase.setNumber(value = response.phoneNumber ?: "")
                isSaved.invoke(true)
            }
        }
    }

}
