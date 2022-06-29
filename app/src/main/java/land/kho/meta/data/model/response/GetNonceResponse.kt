package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetNonceResponse(
    @Json(name = "nonce")
    val nonce: Int
)