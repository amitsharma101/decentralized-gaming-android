package land.kho.wallet

data class KhoWalletAccount(
    var accountAddress: String,
    var mnemonic: String,
    var privateKeyParse: String,
    var publicKeyParse: String
)