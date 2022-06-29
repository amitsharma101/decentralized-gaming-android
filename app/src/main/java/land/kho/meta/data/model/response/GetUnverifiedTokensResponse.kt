package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetUnverifiedTokensResponse(

    @Json(name="count")
    val count: Int,

    @Json(name="userId")
    val userId: Int
)
