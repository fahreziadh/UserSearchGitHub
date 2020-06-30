package dev.ajiex.usergithub.api

import dev.ajiex.usergithub.model.SearchUserResponse
import retrofit2.Response

class UserRepository(private val api: Api) {
    suspend fun searchUser(query: String, page: Int, per_page: Int): Response<SearchUserResponse> {
        val res = api.getSearchUser(query, page, per_page).await()
        return res
    }
}