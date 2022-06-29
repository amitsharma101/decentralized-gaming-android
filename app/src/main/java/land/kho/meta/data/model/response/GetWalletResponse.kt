package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class GetWalletResponse(

	@Json(name="walletBalance")
	val walletBalance: Double,

	@Json(name="inProcessBalance")
	val inProcessBalance: Double
)
