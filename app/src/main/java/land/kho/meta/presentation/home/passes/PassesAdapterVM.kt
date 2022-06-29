package land.kho.meta.presentation.home.passes

import land.kho.meta.data.model.response.GetPassesResponse
import land.kho.meta.utils.AdapterViewModel

class PassesAdapterVM(
    val model: GetPassesResponse,
    override val viewType: Int
) : AdapterViewModel

