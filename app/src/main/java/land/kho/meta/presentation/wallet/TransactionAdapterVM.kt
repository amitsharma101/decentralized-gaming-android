package land.kho.meta.presentation.wallet

import land.kho.meta.data.model.response.Transaction
import land.kho.meta.utils.AdapterViewModel

class TransactionAdapterVM(
    val model: Transaction,
    override val viewType: Int
) : AdapterViewModel

