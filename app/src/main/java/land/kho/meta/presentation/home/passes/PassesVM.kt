package land.kho.meta.presentation.home.passes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import land.kho.meta.base.BaseVM
import land.kho.meta.data.api.NetworkResponse
import land.kho.meta.data.model.response.GetPassesResponse
import land.kho.meta.domain.repository.Repository
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import javax.inject.Inject

@HiltViewModel
class PassesVM @Inject constructor(
    private val repository: Repository
) : BaseVM() {

    val passesList = MutableLiveData<Event<GetPassesResponse?>>()

    fun getPasses() {
        viewModelScope.launch {
            when (val response = repository.getPasses()) {
                is NetworkResponse.Success -> {
                    passesList.postValue(Event(content = response.body))
                }
                is NetworkResponse.Error -> {
                    Utility.showToast(msg = response.message ?: "")
                }
            }
        }
    }
}