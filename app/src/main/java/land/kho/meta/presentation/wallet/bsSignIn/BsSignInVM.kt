package land.kho.meta.presentation.wallet.bsSignIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.GetAccessTokenRequest
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Utility
import javax.inject.Inject


@HiltViewModel
class BsSignInVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    val text = MutableLiveData("")
    val nonce = MutableLiveData<String>()
    var message = ""

    val token = MutableLiveData<String>()

    fun getNonce() {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    Utility.log(tag = "My Public Address ", msg = it)
                    when (val response = repository.getNonce(it)) {
                        is NetworkResponse.Error -> {
                            //TODO Handle Error
                        }
                        is NetworkResponse.Success -> {
                            val newNonce = response.body?.nonce
                            message = "I am signing my one-time nonce: $newNonce"
                            nonce.value = newNonce.toString()
                        }
                    }
                }
            }
        }
    }

    fun getToken(signature: String) {
//        viewModelScope.launch {
//            preferencesUseCase.getAccountAddress().collect {
//                val response = repository.getAccessToken(
//                    GetAccessTokenRequest(signature = signature, publicAddress = it!!)
//                )
//
//                when (response) {
//                    is NetworkResponse.Error -> {
//                        //TODO Handle Error
//                    }
//                    is NetworkResponse.Success -> {
//                        val responseToken = response.body?.accessToken.toString()
//                        preferencesUseCase.setToken(responseToken)
//                        token.value = responseToken
//                    }
//                }
//
//            }
//        }
    }
}
