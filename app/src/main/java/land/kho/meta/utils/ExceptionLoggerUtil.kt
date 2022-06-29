package land.kho.meta.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

class ExceptionLoggerUtil {
    companion object {
        fun logException(throwable: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        }

        fun log(message: String) {
            FirebaseCrashlytics.getInstance().log(message)
        }
    }
}