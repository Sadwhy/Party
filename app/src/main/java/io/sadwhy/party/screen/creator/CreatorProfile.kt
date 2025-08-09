package io.sadwhy.party.screen.creator

import io.sadwhy.party.data.model.Post
import io.sadwhy.party.data.model.Creator
import kotlinx.serialization.Serializable

@Serializable
data class CreatorProfile(
  val post: Post,
  val creator: Creator? = null
)