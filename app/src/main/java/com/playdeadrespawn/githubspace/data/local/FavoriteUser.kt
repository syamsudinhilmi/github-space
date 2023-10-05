package com.playdeadrespawn.githubspace.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
    val login: String,
    @PrimaryKey val id: Int,
    val avatarUrl : String
): Parcelable
