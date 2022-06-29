package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetAccessTokenResponse(
    @Json(name = "accessToken")
    val accessToken: String
)