package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetProfileDataResponse(

    @Json(name = "id")
    val id: Int,

    @Json(name = "nonce")
    val nonce: Int,

    @Json(name = "publicAddress")
    val publicAddress: String,

    @Json(name = "email")
    val email: String?,

    @Json(name = "characterId")
    val characterId: String?,

    @Json(name = "nickname")
    val nickname: String,

    @Json(name = "verified")
    val verified: Boolean,

    @Json(name = "referralCode")
    val referralCode: String,

    @Json(name = "isReferralCompleted")
    val isReferralCompleted: Boolean,

    @Json(name = "referral")
    val referral: SendReferralResponse?,

    @Json(name = "lastLogin")
    val lastLogin: String,

    @Json(name = "createdAt")
    val createdAt: String,

    @Json(name = "updatedAt")
    val updatedAt: String,

    @Json(name = "phoneNumber")
    val phoneNumber: String?

)