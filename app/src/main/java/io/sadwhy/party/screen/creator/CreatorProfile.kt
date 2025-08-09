package io.sadwhy.party.screen.creator

import kotlinx.serialization.Serializable

@Serializable
data class CreatorProfile(
  post: Post,
  creator: Creator?
)