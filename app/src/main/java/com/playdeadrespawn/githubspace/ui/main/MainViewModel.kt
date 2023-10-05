package com.playdeadrespawn.githubspace.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playdeadrespawn.githubspace.data.response.GithubResponse
import com.playdeadrespawn.githubspace.data.response.User
import com.playdeadrespawn.githubspace.data.retrofit.ApiConfig
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class MainViewModel: ViewModel() {

    val listUser = MutableLiveData<ArrayList<User>>()

    fun setSearchUser(query: String) {
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if(response.isSuccessful) {
                    listUser.postValue(response.body()?.items)
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                Log.d(TAG,"onFailure, ${t.message}")
            }
        })
    }
    fun getSearchUser(): LiveData<ArrayList<User>>{
        return listUser
    }
    companion object {
        private const val TAG = "MainViewModel"
    }
}