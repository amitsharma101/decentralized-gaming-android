package land.kho.meta.presentation.home.bsUserName

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.request.UpdateEmailRequest
import land.kho.meta.data.model.response.GetProfileDataResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class BsUserNameVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    var name: String? = null
    val isHavingName = ObservableField(false)

    fun newName() {
        isHavingName.set(false)
    }

    fun changeName(
        name: String,
        isSend: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            when (val response = repository.updateUserName(
                request = UpdateEmailRequest(nickname = name)
            )) {
                is NetworkResponse.Success -> {
                    Utility.showToast(msg = "User name changed")
                    saveInDB(response = response.body!!) {
                        isSend.invoke(true)
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
                preferencesUseCase.setUserName(value = response.nickname)
                withContext(Dispatchers.Main) {
                    isSaved.invoke(true)
                }
            }
        }
    }
}