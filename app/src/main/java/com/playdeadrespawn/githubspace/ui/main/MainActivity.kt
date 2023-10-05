package com.playdeadrespawn.githubspace.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.playdeadrespawn.githubspace.R
import com.playdeadrespawn.githubspace.data.response.User
import com.playdeadrespawn.githubspace.databinding.ActivityMainBinding
import com.playdeadrespawn.githubspace.ui.detail.DetailUserActivity
import com.playdeadrespawn.githubspace.ui.favorite.FavoriteActivity
import com.playdeadrespawn.githubspace.ui.theme.SettingPreferences
import com.playdeadrespawn.githubspace.ui.theme.ThemeActivity
import com.playdeadrespawn.githubspace.ui.theme.dataStore
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var pref: SettingPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallBack(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    searchUser()
                    false
                }
            if (savedInstanceState == null){
                fetchInitialUsers()
            }
            searchBar.text = ""
        }

        viewModel.getSearchUser().observe(this ){
            if (it!=null) {
                adapter.setList(it)
                showLoading(false)
            }
        }

        pref = SettingPreferences.getInstance(application.dataStore)
        lifecycleScope.launch() {
            pref.getThemeSetting().collect { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun fetchInitialUsers() {
        val initialQuery = generateRandomUsername()
        binding.searchBar.text = initialQuery
        searchUser()
    }

    private fun generateRandomUsername(): String {
        val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val randomIndex = (0 until alphabet.length).random()
        return alphabet[randomIndex].toString()
    }

    private fun searchUser() {
        binding.apply {
            val query = searchBar.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
            R.id.settings -> {
                val intent = Intent(this, ThemeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}