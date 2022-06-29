package land.kho.meta.presentation.home.earning

import land.kho.meta.data.model.response.EarningItem
import land.kho.meta.utils.AdapterViewModel

class EarningsAdapterVM(
    val model: EarningItem,
    override val viewType: Int
) : AdapterViewModel

enum class EarningType(val id: String, val value: String) {
    GAME_PAYMENT("GAME_PAYMENT", "game_payment"),
    GAME_REWARD("GAME_REWARD", "game_reward"),
    INVALID("INVALID", "invalid"),
    TRANSFER_TO_WALLET("TRANSFER_TO_WALLET", "transfer_to_wallet"),
    REFERRAL_REWARD ("REFERRAL_REWARD", "referral_reward")
}