package land.kho.meta.presentation.meta

import android.webkit.JavascriptInterface
import land.kho.meta.utils.Utility

const val INTERFACE_NAME = "Android"

class WebAppInterface(
    private var webAppListener: WebAppListener?
) {
    @JavascriptInterface
    fun travelToGame(channelId: String) {
        Utility.log(tag = "FromWeb", msg = "Travel to game called")
        webAppListener?.travelToGame(channelId)
    }

    @JavascriptInterface
    fun openWallet() {
        Utility.log(tag = "FromWeb", msg = "openWallet")
        webAppListener?.openWallet()
    }

    @JavascriptInterface
    fun metaLoadingFinished() {
        Utility.log(tag = "FromWeb", msg = "Loaded")
        webAppListener?.metaLoadingFinished()
    }

    interface WebAppListener {
        fun travelToGame(channelId: String)
        fun openWallet()
        fun metaLoadingFinished()
    }
}