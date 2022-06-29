package land.kho.meta.presentation.avatarselection

import android.annotation.SuppressLint
import android.view.View
import android.webkit.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebViewAssetLoader
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentAvatarSelectionBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility

@AndroidEntryPoint
class AvatarSelectionFragment : BaseFragment<FragmentAvatarSelectionBinding, AvatarSelectionVM>(),
    AvatarSelectionWebAppInterface.WebAppListener {

    private val avatarSelectionVM: AvatarSelectionVM by viewModels()
    private val args: AvatarSelectionFragmentArgs by navArgs()

    override fun layoutRes() = R.layout.fragment_avatar_selection

    override fun viewModelClass() = avatarSelectionVM

    @SuppressLint("SetJavaScriptEnabled")
    override fun bindViews() {
        binding.viewModel = viewModel
        binding.webView.loadUrl("https://kho.land/internal/android/#/avatar?token=${args.token}")
        setWebView(binding.webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setWebView(webView: WebView) {
        val webSettings = webView.settings
        webView.setBackgroundColor(Utility.getColor(R.color.yellow))
        // Setup asset loader to handle local asset paths
        val assetLoader = WebViewAssetLoader.Builder()
            .setDomain("kho.land")
            .addPathHandler(
                "/internal/android/assets/",
                WebViewAssetLoader.AssetsPathHandler(requireActivity())
            )
            .build()

        webView.addJavascriptInterface(
            AvatarSelectionWebAppInterface(webAppListener = this),
            INTERFACE_NAME
        )
        webSettings.domStorageEnabled = true
        webSettings.allowFileAccess = true
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.mediaPlaybackRequiresUserGesture = false
        WebView.setWebContentsDebuggingEnabled(false)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webSettings.javaScriptEnabled = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Override WebView client, and if request is to local file, intercept and serve local
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dismissLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                onAvatarSaveError()
            }
        }

    }

    override fun onAvatarSaved() {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.AVATAR,
            event = AnalyticsConstants.Event.AVATAR_SELECTED,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            findNavController().navigate(
                AvatarSelectionFragmentDirections.actionAvatarSelectionFragmentToMetaFragment(
                    args.token
                )
            )
        }
    }

    override fun onAvatarSaveError() {

    }


}