package land.kho.meta.data.model.request

import com.squareup.moshi.Json

data class UpdateEmailRequest(
    @Json(name = "nickname")
    val nickname: String
)

data class UpdateFcmTokenRequest(
    @Json(name = "fcmToken")
    val fcmToken: String
)