package land.kho.meta.data.model.response

import com.squareup.moshi.Json

data class TransferToWalletResponse(

	@Json(name="success")
	val success: Boolean,

	@Json(name="message")
	val message: String
)
