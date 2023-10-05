package com.playdeadrespawn.githubspace.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.playdeadrespawn.githubspace.data.local.FavoriteUser
import com.playdeadrespawn.githubspace.data.local.FavoriteUserDao
import com.playdeadrespawn.githubspace.data.local.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUsers()
    }
}