package land.kho.meta.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response

const val ERROR = "error"

abstract class ApiRequest {

    protected suspend fun <T : Any> apiRequest(
        call: suspend () -> Response<T>
    ): NetworkResponse<T> {
        return try {
            val response = withContext(Dispatchers.IO) {
                call()
            }
            withContext(Dispatchers.Main) {
                when {
                    response.isSuccessful -> {
                        NetworkResponse.Success(
                            body = response.body()
                        )
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string()
                        var message = "Something went wrong!"
                        if (!errorJsonString.isNullOrEmpty()) {
                            val jsonObject = JSONObject(errorJsonString)
                            message = jsonObject.optString(ERROR)
                        }
                        NetworkResponse.Error(
                            message = message
                        )
                    }
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                NetworkResponse.Error(message = e.message ?: e.toString())
            }
        }
    }
}