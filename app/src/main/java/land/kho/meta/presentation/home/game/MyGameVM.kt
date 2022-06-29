package land.kho.meta.presentation.home.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.UpdateFcmTokenRequest
import land.kho.meta.data.model.response.GetProfileDataResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Constants
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class MyGameVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    lateinit var authToken: String
    val token = MutableLiveData<Event<String>>()
    val userStatus = MutableLiveData<Event<String>>()
    fun getToken() {
        viewModelScope.launch {
            preferencesUseCase.getToken().collect {
                if (!it.isNullOrBlank()) {
                    authToken = it
                    token.postValue(Event(content = it))
                }
            }
        }
    }

    fun checkAvatarStatus() {
        viewModelScope.launch {
            when (val response = repository.getMe()) {
                is NetworkResponse.Success -> {
                    saveInDB(response = response.body!!) {
                        if (response.body.characterId.isNullOrBlank()) {
                            userStatus.postValue(Event(content = Constants.AVATAR_SELECTION))
                        } else {
                            userStatus.postValue(Event(content = Constants.META))
                        }
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
                preferencesUseCase.setUserId(value = response.id)
                preferencesUseCase.setIsVerified(value = response.verified)
                preferencesUseCase.setUserName(value = response.nickname)
                preferencesUseCase.setReferralCode(value = response.referralCode)
                preferencesUseCase.setIsReferralCompleted(value = response.isReferralCompleted)
                preferencesUseCase.setReferredBy(value = response.referral?.referrerUser?.referralCode ?: "")
                preferencesUseCase.setNumber(value = response.phoneNumber ?: "")
                isSaved.invoke(true)
            }
        }
    }



    fun syncFcmTokenWithBackendIfNotAlreadySynced() {
        viewModelScope.launch {
            preferencesUseCase.isNewFcmTokenSynced().collect { it ->
                val isSynced = it ?: false

                    if (!isSynced) {
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                return@OnCompleteListener
                            }
                            // Get new FCM registration token
                            val token = task.result

                            viewModelScope.launch {
                                val response = repository.updateFcmToken(
                                    UpdateFcmTokenRequest(fcmToken = token)
                                )
                                when(response){
                                    is NetworkResponse.Error -> {
                                        preferencesUseCase.setIsNewFcmTokenSynced(false)
                                    }
                                    is NetworkResponse.Success -> {
                                        preferencesUseCase.setIsNewFcmTokenSynced(true)
                                    }
                                }
                            }
                    })
                }
            }
        }

    }

}