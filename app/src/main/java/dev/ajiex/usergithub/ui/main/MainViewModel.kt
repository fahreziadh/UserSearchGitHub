package dev.ajiex.usergithub.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.squareup.moshi.Moshi
import dev.ajiex.usergithub.api.ApiFactory
import dev.ajiex.usergithub.api.UserRepository
import dev.ajiex.usergithub.model.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class MainViewModel : ViewModel() {
    fun searchUser(query: String, page: Int, per_page: Int) = liveData {
        try {
            var repo = UserRepository(ApiFactory.api)
            withContext(Dispatchers.IO) {
                var res = repo.searchUser(query, page, per_page)
                if (!res.isSuccessful) {
                    var err = getError(res.errorBody())
                    emit(err)
                    return@withContext
                } else {
                    emit(res.body())
                }
            }
        } catch (e: Exception) {
            emit(ErrorResponse(1, e.message!!))
        }
    }


    suspend fun getError(errorBody: ResponseBody?): ErrorResponse? {
        return withContext(Dispatchers.Default) {
            var json = errorBody!!.string()
            var moshi = Moshi.Builder().build()
            var jsonAdapter = moshi.adapter(ErrorResponse::class.java)
            return@withContext jsonAdapter.fromJson(json)
        }
    }

}