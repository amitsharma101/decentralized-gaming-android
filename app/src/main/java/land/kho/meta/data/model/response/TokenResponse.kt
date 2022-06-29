package land.kho.meta.data.model.response

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

data class TokenResponse(

    @Json(name = "result")
    val result: List<Transaction>,

    @Json(name = "message")
    val message: String,

    @Json(name = "status")
    val status: String
)

@Parcelize
data class Transaction(

    @Json(name = "blockHash")
    val blockHash: String,

    @Json(name = "tokenSymbol")
    val tokenSymbol: String,

    @Json(name = "tokenName")
    val tokenName: String,

    @Json(name = "contractAddress")
    val contractAddress: String,

    @Json(name = "transactionIndex")
    val transactionIndex: String,

    @Json(name = "confirmations")
    val confirmations: String,

    @Json(name = "nonce")
    val nonce: String,

    @Json(name = "timeStamp")
    val timeStamp: String,

    @Json(name = "input")
    val input: String,

    @Json(name = "gasUsed")
    val gasUsed: String,

    @Json(name = "blockNumber")
    val blockNumber: String,

    @Json(name = "gas")
    val gas: String,

    @Json(name = "tokenDecimal")
    val tokenDecimal: String,

    @Json(name = "cumulativeGasUsed")
    val cumulativeGasUsed: String,

    @Json(name = "from")
    val from: String,

    @Json(name = "to")
    val to: String,

    @Json(name = "value")
    val value: String? =null,

    @Json(name = "hash")
    val hash: String,

    @Json(name = "gasPrice")
    val gasPrice: String,

    @Json(name="tokenID")
    val tokenID: String? =null,

    var balance: String = "",

    var type: String = "",

    var sender: String = "",

    var date: String = ""

) : Parcelable
