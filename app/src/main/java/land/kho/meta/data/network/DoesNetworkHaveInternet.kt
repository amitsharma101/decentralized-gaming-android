package land.kho.meta.data.network

import land.kho.meta.utils.Utility
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.net.SocketFactory

/**
 * Static class Send a ping to googles primary DNS via socket
 * If successful, that means we have internet.
 */

object DoesNetworkHaveInternet {

    /**
     * Make sure to execute this on a background thread.
     */
    @Synchronized
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            execute(
                socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            )
        } catch (e: IOException) {
            Utility.log(tag = TAG, msg = "No internet connection. $e")
            false
        }
    }

    /**
     * Make sure to execute this on a background thread.
     */
    @Synchronized
    fun execute(socket: Socket): Boolean {
        return try {
            Utility.log(tag = TAG, msg = "PINGING google.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Utility.log(tag = TAG, msg = "PING success.")
            true
        } catch (e: IOException) {
            Utility.log(tag = TAG, msg = "No internet connection. $e")
            false
        }
    }
}