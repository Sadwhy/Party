package io.sadwhy.party.data.model

import kotlinx.serialization.Serializable

/**
 * A generic wrapper to hold data of any type `T` and the domain it came from.
 */
@Serializable
data class BaseModel<T>(
    val domain: String,
    val data: T
)