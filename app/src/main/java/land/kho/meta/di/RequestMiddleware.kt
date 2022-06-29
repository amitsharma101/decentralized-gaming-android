package land.kho.meta.di

import android.content.Context
import land.kho.meta.domain.usecases.PreferencesUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RequestMiddleware @Inject constructor(
    @ApplicationContext val context: Context,
    private val preferencesUseCase: PreferencesUseCase
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        runBlocking { addHeader(requestBuilder) }
        val request = requestBuilder.build()
        return chain.proceed(request)
    }


    private suspend fun addHeader(requestBuilder: Request.Builder) {
        requestBuilder.apply {
//             Authorisation
            preferencesUseCase.getToken().first()?.let {
                if (it.isNotEmpty()) {
                    addHeader(AUTHORIZATION, "Bearer $it")
                }
            }

        }
    }

    companion object {
        // Authorisation
        const val AUTHORIZATION = "authorization"
    }

}
