package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetPassesResponse(

    @Json(name = "batteryLeft")
    val batteryLeft: Int,

    @Json(name = "passLimit")
    val passLimit: Int,

    @Json(name = "id")
    val id: Int,

    @Json(name = "type")
    val type: String,

    @Json(name = "userId")
    val userId: Int
)
