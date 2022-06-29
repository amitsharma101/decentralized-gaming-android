package land.kho.meta.presentation.wallet.bsVerify

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class BsVerifyVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    private var userId = 0
    var email: String? = null
    val isHavingEmail = ObservableField(false)

    init {
        getUserId()
    }

    fun newEmail() {
        isHavingEmail.set(false)
    }

    private fun getUserId() {
        viewModelScope.launch {
            preferencesUseCase.getUserId().collect {
                it?.let {
                    userId = it
                }
            }
        }
    }

    fun sendMail(
        email: String,
        isSend: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            when (val response = repository.sendEmail(
                userId = userId,
                email = email
            )) {
                is NetworkResponse.Success -> {
                    Utility.showToast(msg = "Verification link has been send")
                    saveInDB(email = email) {
                        isSend.invoke(true)
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.body?.message ?: "")
                    isSend.invoke(true)
                }
            }
        }
    }

    private fun saveInDB(
        email: String,
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setTempEmail(value = email)
                isSaved.invoke(true)
            }
        }
    }
}