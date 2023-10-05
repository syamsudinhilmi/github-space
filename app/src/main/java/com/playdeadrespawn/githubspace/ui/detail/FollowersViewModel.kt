package com.playdeadrespawn.githubspace.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playdeadrespawn.githubspace.data.response.User
import com.playdeadrespawn.githubspace.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setListFollowers(username: String) {
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<User>>{
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    listFollowers.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d(FollowersViewModel.TAG, "onFailure, ${t.message}"  )
            }
        })
    }

    fun getListFollowers(): LiveData<ArrayList<User>>{
        return listFollowers
    }
    companion object {
        private const val TAG = "FollowersViewModel"
    }
}