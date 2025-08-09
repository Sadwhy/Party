package io.sadwhy.party.screen.Search

import kotlinx.serialization.Serializable

@Serializable
data class Search(val title: String, val text: String)