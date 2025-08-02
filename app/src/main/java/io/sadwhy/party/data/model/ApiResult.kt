package io.sadwhy.party.data.model

/**
 * A sealed class to represent the result of an API call.
 */
sealed class ApiResult<out T> {

    /**
     * Represents a successful API result.
     * @param domain The domain or source of the data (e.g., "kemono", "coomer").
     * @param data The actual data returned from the API.
     */
    data class Success<T>(
        val data: T,
        val domain: String
    ) : ApiResult<T>()

    /**
     * Represents an API failure with an HTTP code and message.
     */
    data class Failure(
        val code: Int,
        val message: String
    ) : ApiResult<Nothing>()

    /**
     * Represents an exception that occurred during the API call.
     */
    data class Exception(
        val throwable: Throwable
    ) : ApiResult<Nothing>()
}