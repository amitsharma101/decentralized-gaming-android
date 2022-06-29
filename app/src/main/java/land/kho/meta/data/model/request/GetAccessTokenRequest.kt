package land.kho.meta.data.model.request

import com.squareup.moshi.Json

data class GetAccessTokenRequest(
    @Json(name = "signature")
    val signature: String,

    @Json(name = "publicAddress")
    val publicAddress: String,

    @Json(name = "phoneNumber")
    val phoneNumber: String?,

    @Json(name = "fcmToken")
    val fcmToken: String?
)