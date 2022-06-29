package land.kho.meta.presentation.wallet.bsReceipt

import land.kho.meta.R
import land.kho.meta.data.model.response.Transaction
import land.kho.meta.utils.AdapterViewModel

class ReceiptAdapterVM(
    val model: ReceiptModel
) : AdapterViewModel {
    override val viewType: Int
        get() = R.layout.item_receipt
}

data class ReceiptModel(
    val title: String,
    val titleValue: String
)

data class ReceiptData(
    val to: String? = null,
    val from: String? = null,
    val amount: String? = null,
    val model: Transaction? = null
)

data class Receipt(
    val type: String,
    val to: String,
    val from: String,
    val amount: String,
    val link: String? = null,
    val id: String? = null
)

