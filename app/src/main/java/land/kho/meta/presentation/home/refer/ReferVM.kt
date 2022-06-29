package land.kho.meta.presentation.home.refer

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.UpdateReferralCodeRequest
import land.kho.meta.data.model.response.GetProfileDataResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class ReferVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    var shareImage: Bitmap? = null
    var myReferralCode = MutableLiveData("")
    var referredBy = MutableLiveData("")
    var isReferralCompleted = MutableLiveData(false)
    val userStatus = MutableLiveData<Event<String>>()

    init {
        getReferralCode()
        getIsReferralCompleted()
    }

    private fun getReferralCode() {
        viewModelScope.launch {
            preferencesUseCase.getReferralCode().collect {
                if (!it.isNullOrBlank()) {
                    myReferralCode.value = it
                } else {
                    getMe()
                }
            }
        }
    }

    private fun getIsReferralCompleted() {
        viewModelScope.launch {
            preferencesUseCase.getIsReferralCompleted().collect {
                it?.let {
                    isReferralCompleted.value = it
                    getReferredBy()
                }
            }
        }
    }

    private fun getReferredBy() {
        viewModelScope.launch {
            preferencesUseCase.getReferredBy().collect {
                if (!it.isNullOrBlank()) {
                    referredBy.value = it
                }
            }
        }
    }

    fun putReferralCode(
        referralCode: String
    ) {
        viewModelScope.launch {
            when (val response = repository.updateReferralCode(
                request = UpdateReferralCodeRequest(referralCode = referralCode)
            )) {
                is NetworkResponse.Success -> {
                    Utility.showToast(msg = "success")
                    saveIsReferralCompleted(referredBy = referralCode) {
                        isReferralCompleted.postValue(true)
                        referredBy.postValue(referralCode)
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    private fun getMe() {
        viewModelScope.launch {
            when (val response = repository.getMe()) {
                is NetworkResponse.Success -> {
                    saveInDB(response = response.body!!) {
                        myReferralCode.postValue(response.body.referralCode)
                        referredBy.postValue(response.body.referral?.referrerUser?.referralCode ?: "")
                        isReferralCompleted.postValue(response.body.isReferralCompleted)
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
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
                preferencesUseCase.setIsVerified(value = response.verified)
                preferencesUseCase.setUserId(value = response.id)
                preferencesUseCase.setUserName(value = response.nickname)
                preferencesUseCase.setReferralCode(value = response.referralCode)
                preferencesUseCase.setIsReferralCompleted(value = response.isReferralCompleted)
                preferencesUseCase.setReferredBy(value = response.referral?.referrerUser?.referralCode ?: "")
                preferencesUseCase.setNumber(value = response.phoneNumber ?: "")
                isSaved.invoke(true)
            }
        }
    }

    private fun saveIsReferralCompleted(
        referredBy: String,
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setIsReferralCompleted(value = true)
                preferencesUseCase.setReferredBy(value = referredBy)
                isSaved.invoke(true)
            }
        }
    }
}