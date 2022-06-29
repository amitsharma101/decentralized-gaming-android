package land.kho.meta.presentation.game

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import land.kho.meta.R
import land.kho.meta.base.BaseFragment
import land.kho.meta.databinding.FragmentGameBinding
import land.kho.meta.utils.AnalyticsConstants
import land.kho.meta.utils.AnalyticsLogger
import land.kho.meta.utils.Constants
import land.kho.meta.utils.Utility

@AndroidEntryPoint
class GameFragment : BaseFragment<FragmentGameBinding, GameVM>(),
    GameWebAppInterface.WebAppListener {

    private val gameVM: GameVM by viewModels()
    private val args: GameFragmentArgs by navArgs()

    override fun layoutRes() = R.layout.fragment_game

    override fun viewModelClass() = gameVM

    override fun bindViews() {
        binding.viewModel = viewModel
        binding.webView.loadUrl("https://kho.land/internal/android/#/ludo?token=${args.token}")
        setWebView(binding.webView)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView(webView: WebView) {
        webView.setBackgroundColor(Utility.getColor(R.color.yellow))
        webView.webViewClient = object : WebViewClient() {

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
            }
        }

        val webSettings = webView.settings
        webView.addJavascriptInterface(
            GameWebAppInterface(webAppListener = this),
            INTERFACE_NAME
        )   
        webSettings.domStorageEnabled = true
        WebView.setWebContentsDebuggingEnabled(false)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webSettings.javaScriptEnabled = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyWebView()
    }

    private fun destroyWebView() {
        binding.webView.loadUrl("about:blank")
        binding.webView.destroy()
    }

    override fun travelToHome() {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.LUDO,
            event = AnalyticsConstants.Event.GO_TO_HOME,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            findNavController().navigate(R.id.action_game_fragment_to_my_game_fragment)
        }
    }

    override fun joinDiscord() {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.LUDO,
            event = AnalyticsConstants.Event.GO_TO_DISCORD,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            openDiscord(Constants.DISCORD_LINK)
        }
    }

    override fun refer() {
        AnalyticsLogger.logEvent(
            screen = AnalyticsConstants.ScreenName.LUDO,
            event = AnalyticsConstants.Event.GO_TO_REFER,
            value = AnalyticsConstants.Value.BUTTON_CLICKED
        )
        activity?.runOnUiThread {
            findNavController().navigate(R.id.action_game_fragment_to_refer_fragment)
        }
    }

    private fun openDiscord(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }


}