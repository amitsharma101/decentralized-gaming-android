package land.kho.meta.presentation.meta

import android.annotation.SuppressLint
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.webkit.WebViewAssetLoader
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentMetaBinding
import land.kho.meta.presentation.wallet.bsSummary.BsSummaryFragment
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Utility
import land.kho.meta.utils.visibleOrGone


@AndroidEntryPoint
class MetaFragment : BaseFragment<FragmentMetaBinding, MetaVM>(), WebAppInterface.WebAppListener {

    private val args: MetaFragmentArgs by navArgs()
    private var isError = false
    private var isLoaded = false
    private val metaVM: MetaVM by viewModels()

    override fun layoutRes() = R.layout.fragment_meta

    override fun viewModelClass() = metaVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.webView.loadUrl("https://kho.land/internal/android/#/meta?token=${args.token}")
        setWebView(binding.webView)
        setUpClickListener()
    }

    private fun setUpClickListener() {
        binding.clError.btnReload.setOnClickListener {
            AnalyticsLogger.logEvent(
                screen = AnalyticsConstants.ScreenName.META,
                event = AnalyticsConstants.Event.RELOAD,
                value = AnalyticsConstants.Value.BUTTON_CLICKED
            )
            binding.webView.reload()
            isError = false
        }
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

        webView.addJavascriptInterface(WebAppInterface(webAppListener = this), INTERFACE_NAME)
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
                if (!isError) {
                    binding.clError.root.visibleOrGone(isVisible = false)
                    binding.webView.visibleOrGone(isVisible = true)
                }
                dismissLoading()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                showError(error = "No Internet")
            }
        }
    }

    private fun showError(error: String) {
        if (!isLoaded) {
            binding.clError.root.visibleOrGone(isVisible = true)
            binding.webView.visibleOrGone(isVisible = false)
            // binding.clError.tvError.text = error
            isError = true
        }
    }

    override fun openWallet() {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.META,
            event = AnalyticsConstants.Event.GO_TO_WALLET,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            BsSummaryFragment.getNewInstance(
                accountAddress = viewModel.accountAddress,
                balance = viewModel.balance,
                unverifiedTokens = if (viewModel.isVerified) "" else viewModel.unverifiedTokens,
                isVerified = viewModel.isVerified
            ).show(childFragmentManager, "BsSummaryFragment")
        }

    }

    override fun metaLoadingFinished() {
        isLoaded = true
    }

    override fun travelToGame(channelId: String) {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.META,
            event = AnalyticsConstants.Event.TELEPORTED_TO_GAME,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            Toast.makeText(
                context,
                String.format("TAKING YOU TO GAME. [%s]", channelId),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(
                MetaFragmentDirections.actionMetaFragmentToGameFragment(
                    args.token
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyWebView()
    }

    private fun destroyWebView() {
        binding.webView.loadUrl("about:blank")
        binding.webView.destroy()
    }

}