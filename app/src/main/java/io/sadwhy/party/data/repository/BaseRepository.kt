package io.sadwhy.party.data.repository

import retrofit2.Response
import io.sadwhy.party.data.model.ApiResult
import io.sadwhy.party.data.model.ErrorResponse
import io.sadwhy.party.network.JsonProvider

/**
 * A base repository class that standardizes API call execution and error parsing.
 */
open class BaseRepository {

    protected val json = JsonProvider.json

    /**
     * Executes an API call and wraps the result in [ApiResult].
     *
     * @param T The type of the expected successful response body.
     * @param apiCall A suspend lambda that executes the Retrofit call.
     * @return [ApiResult] containing either success with data, a failure with message, or an exception.
     */
    protected suspend fun <T> call(apiCall: suspend () -> Response<T>): ApiResult<T> {
        return runCatching { apiCall() }.getOrElse { throwable ->
            return ApiResult.Exception(throwable)
        }.let { response ->
            val code = response.code()
            val domain = response.raw().request.url.host
            val body = response.body()
    
            when {
                response.isSuccessful && body != null -> ApiResult.Success(data = body, domain = domain)
                response.isSuccessful -> ApiResult.Failure(code, "Response body is null")
                else -> ApiResult.Failure(code, parseError(response))
            }
        }
    }

    private fun parseError(response: Response<*>): String {
        val rawError = response.errorBody()?.string().orEmpty()

        return try {
            if (rawError.isNotBlank()) {
                json.decodeFromString<ErrorResponse>(rawError).error
            } else {
                response.message().ifBlank { "An unknown error occurred" }
            }
        } catch (e: Exception) {
            response.message().ifBlank { "An unknown error occurred" }
        }
    }

}