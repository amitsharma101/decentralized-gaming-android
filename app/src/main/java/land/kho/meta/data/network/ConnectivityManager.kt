package land.kho.meta.data.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import land.kho.meta.presentation.application.AppApplication
import java.net.Socket
import javax.inject.Singleton

/**
 * Singleton class having single instance of connectionLiveData
 * isNetworkAvailable as live data that can be  observe in ui
 * use isNetworkAvailable to perform network check throughout the app
 */

const val TAG = "C-Manager"

@Singleton
class ConnectivityManager
constructor(
    val context: AppApplication
) {

    /**
     * single instance of connectionLiveData
     */

    private val connectionLiveData = ConnectionLiveData(context)

    /**
     * isNetworkAvailable as live data that can be observe in ui
     * use isNetworkAvailable to perform network check throughout the app
     */

    val isNetworkAvailable = MutableLiveData(true)

    /**
     * register to initialise isNetworkAvailable and observe connectionLiveData
     * when activity is start
     */

    @Synchronized
    fun registerConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.observe(lifecycleOwner) { isConnected ->
            isConnected?.let { isNetworkAvailable.value = it }
        }
    }

    /**
     * unregister to remove observe form connectionLiveData
     * when activity is destroyed
     */

    @Synchronized
    fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner) {
        connectionLiveData.removeObservers(lifecycleOwner)
    }

    /**
     * ping dns for internet connectivity
     */

    @Synchronized
    fun pingDNS() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            val hasInternet = DoesNetworkHaveInternet.execute(socket = Socket())
            withContext(Dispatchers.Main) {
                isNetworkAvailable.value = hasInternet
            }
        }
    }
}