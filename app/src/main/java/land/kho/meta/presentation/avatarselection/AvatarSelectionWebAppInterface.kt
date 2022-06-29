package land.kho.meta.presentation.avatarselection

import android.webkit.JavascriptInterface
import land.kho.meta.utils.Utility

const val INTERFACE_NAME = "Android"

class AvatarSelectionWebAppInterface(
    private var webAppListener: WebAppListener?
) {

    @JavascriptInterface
    fun avatarSaved() {
        Utility.log(tag = "FromWeb", msg = "avatarSaved called")
        webAppListener?.onAvatarSaved()
    }

    @JavascriptInterface
    fun avatarSaveError() {
        Utility.log(tag = "FromWeb", msg = "avatarSaveError called")
        webAppListener?.onAvatarSaveError()
    }

    interface WebAppListener {
        fun onAvatarSaved()
        fun onAvatarSaveError()
    }
}