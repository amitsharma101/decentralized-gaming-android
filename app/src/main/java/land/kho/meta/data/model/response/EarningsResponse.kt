package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class EarningsResponse(

    @Json(name = "data")
    val data: List<EarningItem>,

    @Json(name = "count")
    val count: Int
)

data class EarningItem(

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "amount")
    val amount: Double,

    @Json(name = "id")
    val id: Int,

    @Json(name = "type")
    val type: String,

    @Json(name = "userId")
    val userId: Int,

    @Json(name = "updatedAt")
    val updatedAt: String,

    @Json(name = "metadata")
    val metadata: String? = "",

    var text: String = ""
)
