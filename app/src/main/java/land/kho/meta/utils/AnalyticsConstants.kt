package land.kho.meta.utils


interface AnalyticsConstants {

    interface ScreenName {
        companion object {
            const val WALLET = "WALLET"
            const val META = "META"
            const val GAME = "GAME"
            const val AVATAR= "AVATAR"
            const val HOME= "HOME"
            const val EARNING = "EARNING"
            const val LUDO = "LUDO"
            const val REFER = "REFER"
            const val BS_SIGN = "BS_SIGN"
            const val BS_SEND_MONEY = "BS_SEND_MONEY"
            const val BS_RECEIVE_MONEY = "BS_RECEIVE_MONEY"
            const val BS_WALLET_SUMMARY = "BS_WALLET_SUMMARY"
            const val BS_RECEIPT = "BS_RECEIPT"
            const val BS_SEND_EMAIL = "BS_SEND_EMAIL"
            const val BS_USER_NAME = "BS_USER_NAME"
            const val BS_INFO = "BS_INFO"
        }
    }

    interface Event {
        companion object {
            const val SEND_MONEY = "SEND_MONEY"
            const val RECEIVE_MONEY = "RECEIVE_MONEY"
            const val IMPORT_WALLET = "IMPORT_WALLET"
            const val CREATE_WALLET = "CREATE_WALLET"
            const val MNEMONIC_COPY = "MNEMONIC_COPY"
            const val MNEMONIC_ADDED = "MNEMONIC_ADDED"
            const val MNEMONIC_SHARED = "MNEMONIC_SHARED"
            const val MNEMONIC_SAVED = "MNEMONIC_SAVED"
            const val ENTER_KHOVERSE = "ENTER_KHOVERSE"
            const val ADDRESS_COPY = "ADDRESS_COPY"
            const val TRANSACTION_ID_COPY = "TRANSACTION_ID_COPY"
            const val ADDRESS_SHARED = "ADDRESS_SHARED"
            const val TOKEN_TRANSACTIONS_TAB = "TOKEN_TRANSACTIONS_TAB"
            const val NFT_TRANSACTIONS_TAB = "NFT_TRANSACTIONS_TAB"
            const val GO_TO_WALLET = "GO_TO_WALLET"
            const val ACCEPT = "ACCEPT"
            const val CANCEL = "CANCEL"
            const val TRANSACTION_OPEN_IN_BROWSER = "TRANSACTION_OPEN_IN_BROWSER"
            const val RELOAD = "RELOAD"
            const val TELEPORTED_TO_GAME = "TELEPORTED_TO_GAME"
            const val AVATAR_SELECTED= "AVATAR_SELECTED"
            const val VIEW_TRANSACTION = "VIEW_TRANSACTION"
            const val VERIFY_EMAIL = "VERIFY_EMAIL"
            const val USER_NAME = "USER_NAME"
            const val LUDO_OPENED = "LUDO_OPENED"
            const val PASS = "PASS"
            const val PASS_EXHAUSTED = "PASS_EXHAUSTED"
            const val TRANSFER_KHO = "TRANSFER_KHO"
            const val CHANGE_USER_NAME = "CHANGE_USER_NAME"
            const val GO_TO_HOME = "GO_TO_HOME"
            const val GO_TO_REFER = "GO_TO_REFER"
            const val GO_TO_DISCORD = "GO_TO_DISCORD"
            const val GO_TO_TELEGRAM = "GO_TO_TELEGRAM"
            const val REFERRAL_SHARED = "REFERRAL_SHARED"
            const val ENTER_REFERRAL_CODE = "ENTER_REFERRAL_CODE"
            const val REFERRAL_CODE_COPY = "REFERRAL_CODE_COPY"
            const val LUDO_RULES_DISMISS_CLICKED = "LUDO_RULES_DISMISS_CLICKED"
        }
    }

    interface Value {
        companion object {
            const val BUTTON_CLICKED = "BUTTON_CLICKED"
            const val ITEM_CLICKED = "ITEM_CLICKED"
            const val COPY_CLICKED = "COPY_CLICKED"
            const val TRUE = "TRUE"
        }
    }

}

