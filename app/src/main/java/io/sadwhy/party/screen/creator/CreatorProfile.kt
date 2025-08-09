package io.sadwhy.party.screen.creator

import kotlinx.serialization.Serializable

@Serializable
data class CreatorProfile(
  val creatorId: String,
  val creatorService: String
)