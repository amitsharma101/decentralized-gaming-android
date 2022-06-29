package land.kho.meta.utils

import com.mixpanel.android.mpmetrics.MixpanelAPI
import land.kho.meta.presentation.application.AppApplication
import org.json.JSONObject

object AnalyticsLogger {

    fun logEvent(
        screen: String,
        event: String,
        value: String
    ) {
        MixpanelAPI.getInstance(
            AppApplication.appContext,
            "299900972f8f12718901aa35833f2f1e"
        ).track(screen, JSONObject().put(event, value))
    }
}