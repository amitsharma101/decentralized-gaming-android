package land.kho.meta.presentation.application

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import land.kho.meta.data.network.ConnectivityManager

@HiltAndroidApp
class AppApplication : Application() {

    companion object {
        lateinit var appContext: AppApplication
        lateinit var connectivityManager: ConnectivityManager
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        connectivityManager = ConnectivityManager(context = this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

}