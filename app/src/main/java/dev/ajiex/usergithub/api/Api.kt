package dev.ajiex.usergithub.api

import dev.ajiex.usergithub.model.SearchUserResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/search/users")
    fun getSearchUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Deferred<Response<SearchUserResponse>>
}