package com.playdeadrespawn.githubspace.data.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("following_url")
	val followingUrl: String,

	@field:SerializedName("following")
	val following: Int,

	@field:SerializedName("followers_url")
	val followersUrl: String,

	@field:SerializedName("followers")
	val followers: Int,

	@field:SerializedName("bio")
	val bio: Any,

	@field:SerializedName("name")
	val name: String,
)
