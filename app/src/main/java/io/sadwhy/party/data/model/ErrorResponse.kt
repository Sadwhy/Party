package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

/**
 * Models the expected structure of a JSON error response from the API.
 */
@Serializable
data class ErrorResponse(val error: String)
