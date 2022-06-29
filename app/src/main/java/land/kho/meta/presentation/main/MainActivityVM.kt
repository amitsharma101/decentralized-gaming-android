package land.kho.meta.presentation.main

import androidx.lifecycle.MutableLiveData
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
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class MainActivityVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    lateinit var mnemonic: String
    var userName = MutableLiveData("")
    lateinit var address: String
    val accountAddress = MutableLiveData<Event<String>>()
    val passes = MutableLiveData("")

    private fun getMnemonic() {
        viewModelScope.launch {
            preferencesUseCase.getMnemonic().collect {
                if (!it.isNullOrBlank()) {
                    mnemonic = it
                }
            }
        }
    }

    private fun getPasses(
        pass: (Int) -> Unit
    ) {
        viewModelScope.launch {
            when (val response = repository.getPasses()) {
                is NetworkResponse.Success -> {
                    pass.invoke(response.body?.batteryLeft ?: 0)
                    passes.value = response.body?.batteryLeft.toString()
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    fun getAccountAddress(
        pass: (Int) -> Unit
    ) {
        viewModelScope.launch {
            preferencesUseCase.getAccountAddress().collect {
                if (!it.isNullOrBlank()) {
                    withContext(Dispatchers.Main) {
                        accountAddress.postValue(Event(content = it))
                        address = it
                    }
                    getMnemonic()
                    getPasses { passes ->
                        pass.invoke(passes)
                    }
                    getUserName()
                }
            }
        }
    }

    private fun getUserName() {
        viewModelScope.launch {
            preferencesUseCase.getUserName().collect {
                if (!it.isNullOrBlank()) {
                    userName.value = it
                }
            }
        }
    }
}