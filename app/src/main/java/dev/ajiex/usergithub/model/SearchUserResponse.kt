package dev.ajiex.usergithub.model

data class SearchUserResponse(
    val incomplete_results: Boolean?,
    val items: List<Item>?,
    val total_count: Int?
)