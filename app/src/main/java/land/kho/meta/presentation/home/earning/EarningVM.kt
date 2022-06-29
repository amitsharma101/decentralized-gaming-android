package land.kho.meta.presentation.home.earning

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.response.EarningItem
import land.kho.meta.domain.repository.Repository
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class EarningVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    private val repository: Repository
) : BaseVM() {

    val earningsList = MutableLiveData<Event<List<EarningItem>?>>()
    var balance = MutableLiveData("")
    var inProgressBalance = MutableLiveData("")
    val isHavingInProgressBalance = MutableLiveData(false)
    val isHavingBalance = MutableLiveData(false)
    var isVerified = false

    init {
        getVerified()
    }

    fun getEarnings() {
        viewModelScope.launch {
            when (val response = repository.getEarnings()) {
                is NetworkResponse.Success -> {
                    earningsList.postValue(Event(content = response.body?.data))
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    fun getWalletBalance() {
        viewModelScope.launch {
            when (val response = repository.getWalletBalance()) {
                is NetworkResponse.Success -> {
                    isHavingBalance.value = response.body?.walletBalance!! > 0
                    balance.value = "Total Earnings : ${response.body.walletBalance} KHO"
                    if (response.body.inProcessBalance > 0) {
                        isHavingInProgressBalance.value = true
                        inProgressBalance.value =
                            "We are transferring : ${response.body.inProcessBalance} KHO to your crypto wallet it will take some time"
                    } else {
                        isHavingInProgressBalance.value = false
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    fun transferToWallet() {
        viewModelScope.launch {
            when (val response = repository.transferToWallet()) {
                is NetworkResponse.Success -> {
                    if (response.body?.success == true) {
                        getWalletBalance()
                        getEarnings()
                    } else {
                        Utility.showToast(msg = response.body?.message ?: "")
                    }
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }

    private fun getVerified() {
        viewModelScope.launch {
            preferencesUseCase.getIsVerified().collect {
                it?.let {
                    isVerified = it
                }
            }
        }
    }
}
