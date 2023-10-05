package com.playdeadrespawn.githubspace.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.playdeadrespawn.githubspace.data.response.User
import com.playdeadrespawn.githubspace.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val list = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallBack (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }
    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User){
            binding.root.setOnClickListener{
                onItemClickCallback?.onItemClicked(user)
            }
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivUser)
                tvName.text = user.login
                tvId.text = "ID: ${user.id}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }
    override fun getItemCount(): Int = list.size
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}