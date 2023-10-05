package com.playdeadrespawn.githubspace.ui.detail

import com.playdeadrespawn.githubspace.ui.main.UserAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.playdeadrespawn.githubspace.R
import com.playdeadrespawn.githubspace.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        showLoading(true)

        viewModel.setUserDetail(username)
        viewModel.getUserDetail().observe(this) { userDetail ->
            userDetail.let {
                with(binding) {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text =  resources.getString(R.string.data_follower, it.followers)
                    tvFollowings.text =  resources.getString(R.string.data_following, it.following)

                    tvBio.text = "${it.bio}"
                    tvBio.visibility = if (it.bio != null) View.VISIBLE else View.GONE

                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivProfile)
                }
                showLoading(false)
            }
        }

        var _isCheck =false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                        _isCheck = true
                    } else {
                        binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
                        _isCheck = false
                    }
                }
            }
        }

        binding.fabFavorite.setOnClickListener {
            _isCheck = !_isCheck
            if (_isCheck) {
                if (username != null) {
                    if (avatarUrl != null) {
                        viewModel.addToFavorite(username, id, avatarUrl)
                    }
                }
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                viewModel.removeFromFavorite(id)
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        binding.tab.setupWithViewPager(binding.viewPager)
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }
}
