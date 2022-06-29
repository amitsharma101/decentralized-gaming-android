package land.kho.meta.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Constants
import land.kho.meta.utils.Event
import javax.inject.Inject

@HiltViewModel
class SplashVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {

    val isLoading = MutableLiveData(true)
    val userStatus: LiveData<Event<String>>
        get() = _userStatus
    private val _userStatus = MutableLiveData<Event<String>>()

    init {
        viewModelScope.launch {
            delay(timeMillis = 3000)
            withContext(Dispatchers.Main) {
                when {
                    preferencesUseCase.getNumber().first().isNullOrBlank()-> {
                        _userStatus.postValue(Event(content = Constants.LOGIN))
                    }
                    preferencesUseCase.getIsWalletCreated().first() != true -> {
                        _userStatus.postValue(Event(content = Constants.CREATE_WALLET))
                    }
                    preferencesUseCase.getIsMnemonicSaved().first() != true -> {
                        _userStatus.postValue(Event(content = Constants.MNEMONIC))
                    }
                    else -> {
                        _userStatus.postValue(Event(content = Constants.HOME))
                    }
                }
            }
        }
    }

}