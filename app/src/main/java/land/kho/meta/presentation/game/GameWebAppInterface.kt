package land.kho.meta.presentation.game

import android.webkit.JavascriptInterface
import land.kho.meta.utils.Utility

const val INTERFACE_NAME = "Android"

class GameWebAppInterface(
    private var webAppListener: WebAppListener?
) {

    @JavascriptInterface
    fun backToHome() {
        Utility.log(tag = "FromWeb", msg = "Travel to Home")
        webAppListener?.travelToHome()
    }

    @JavascriptInterface
    fun joinDiscord() {
        Utility.log(tag = "FromWeb", msg = "Join Discord")
        webAppListener?.joinDiscord()
    }

    @JavascriptInterface
    fun refer() {
        Utility.log(tag = "FromWeb", msg = "Refer")
        webAppListener?.refer()
    }

    interface WebAppListener {
        fun travelToHome()
        fun joinDiscord()
        fun refer()
    }
}