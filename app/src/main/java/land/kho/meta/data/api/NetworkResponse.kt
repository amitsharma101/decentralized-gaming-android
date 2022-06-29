package land.kho.meta.data.api

import androidx.annotation.Keep

sealed class NetworkResponse<out T : Any> {

    @Keep
    data class Success<out T : Any>(
        val body: T?
    ) : NetworkResponse<T>()

    @Keep
    data class Error<out T : Any>(
        val body: T? = null,
        val message: String? = null
    ) : NetworkResponse<T>()
}