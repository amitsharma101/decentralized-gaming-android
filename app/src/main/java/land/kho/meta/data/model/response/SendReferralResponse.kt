package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class SendReferralResponse(

    @Json(name = "referrerUser")
    val referrerUser: ReferrerUser? = null,

    @Json(name = "id")
    val id: Int? = null
)

data class ReferrerUser(

    @Json(name = "referralCode")
    val referralCode: String,

    @Json(name = "nickname")
    val nickname: String,

    @Json(name = "publicAddress")
    val publicAddress: String,

    @Json(name = "id")
    val id: Int
)
