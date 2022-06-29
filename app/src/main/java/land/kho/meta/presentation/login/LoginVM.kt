package land.kho.meta.presentation.login

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

    var number = ""

    fun saveInDB(
        isSaved: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                preferencesUseCase.setNumber(value = number)
                withContext(Dispatchers.Main) {
                    isSaved.invoke(true)
                }
            }
        }
    }


}
