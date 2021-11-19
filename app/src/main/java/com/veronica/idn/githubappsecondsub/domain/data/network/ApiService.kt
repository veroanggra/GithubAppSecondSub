package com.veronica.idn.githubappsecondsub.domain.data.network

import com.veronica.idn.githubappsecondsub.BuildConfig
import com.veronica.idn.githubappsecondsub.domain.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    @GET("search/users?q=followers%3A>%3D1000&ref=searchresults&s=followers&type=Users")
    @Headers("Authorization: token ${BuildConfig.KEY}")
    suspend fun getListUser():UserResponse
}