package land.kho.meta.data.model.request

import com.squareup.moshi.Json

data class UpdateReferralCodeRequest(
    @Json(name = "referralCode")
    val referralCode: String
)