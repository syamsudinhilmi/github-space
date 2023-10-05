package com.playdeadrespawn.githubspace.data.retrofit

import com.playdeadrespawn.githubspace.data.response.DetailUserResponse
import com.playdeadrespawn.githubspace.data.response.GithubResponse
import com.playdeadrespawn.githubspace.data.response.User
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: BuildConfig.KEY")
    fun getSearchUser (
        @Query("q") query: String
    ): Call<GithubResponse>


    @GET("users/{username}")
    @Headers("Authorization: BuildConfig.KEY")
    fun getUserDetail (
        @Path("username") username: String?
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: BuildConfig.KEY")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: BuildConfig.KEY")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>


}