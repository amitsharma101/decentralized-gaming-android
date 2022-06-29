package land.kho.meta.presentation.wallet.bsReceipt

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import land.kho.meta.R
import land.kho.meta.base.BaseVM
import land.kho.meta.domain.usecases.PreferencesUseCase
import land.kho.meta.utils.Constants
import land.kho.meta.utils.Event
import land.kho.meta.utils.Utility
import land.kho.meta.utils.addPropertyChangedCallback
import javax.inject.Inject


@HiltViewModel
class BsReceiptVM @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase
) : BaseVM() {


    val receiptListUpdated = MutableLiveData<Event<List<ReceiptModel>>>()
    val isReceipt = MutableLiveData(true)
    var modal = MutableLiveData<Receipt>()

    var receiptData = ObservableField<ReceiptData>().apply {
        addPropertyChangedCallback {
            get()?.let { receipt ->
                if (receipt.model == null) {
                    isReceipt.value = false
                    modal.value = Receipt(
                        type = Constants.SEND_TO,
                        to = receipt.to!!,
                        from = receipt.from!!,
                        amount = receipt.amount!!
                    )
                    updateList(
                        transactionData = mapOf(
                            Utility.getString(R.string.amount) to receipt.amount,
                            Utility.getString(R.string.gas_fee) to "todo",
                            Utility.getString(R.string.gas_limit) to "todo",
                            Utility.getString(R.string.total) to "todo"
                        )
                    )
                } else {
                    isReceipt.value = true
                    modal.value = Receipt(
                        //todo verify
                        type = receipt.model.type,
                        link = "",
                        id = receipt.model.hash,
                        to = receipt.model.to,
                        from = receipt.model.from,
                        amount = receipt.model.balance
                    )
                    updateList(
                        transactionData = mapOf(
                            Utility.getString(R.string.amount) to receipt.model.balance,
                            Utility.getString(R.string.gas_fee) to receipt.model.gasUsed,
                            Utility.getString(R.string.gas_limit) to receipt.model.gas,
                            Utility.getString(R.string.total) to receipt.model.cumulativeGasUsed
                        )
                    )
                }
            }

        }
    }

    fun getMnemonic(
        mnemonic: (name: String) -> Unit
    ) {
        viewModelScope.launch {
            preferencesUseCase.getMnemonic().collect {
                if (!it.isNullOrBlank()) {
                    mnemonic.invoke(it)
                }
            }
        }
    }

    private fun updateList(transactionData: Map<String, String>) {
        val receiptList = mutableListOf<ReceiptModel>()
        transactionData.forEach { place ->
            receiptList.add(
                ReceiptModel(
                    title = place.key,
                    titleValue = place.value
                )
            )
        }
        receiptListUpdated.postValue(Event(content = receiptList))
    }
}
