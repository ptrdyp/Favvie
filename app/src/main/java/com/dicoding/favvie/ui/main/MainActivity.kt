package com.dicoding.favvie.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favvie.R
import com.dicoding.favvie.data.remote.response.ResultsItem
import com.dicoding.favvie.databinding.ActivityMainBinding
import com.dicoding.favvie.ui.detail.DetailActivity
import com.dicoding.favvie.ui.favorite.FavoriteActivity
import com.dicoding.favvie.ui.setting.SettingActivity
import com.dicoding.favvie.ui.setting.SettingPreferences
import com.dicoding.favvie.ui.setting.dataStore
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val settingPreferences = SettingPreferences.getInstance(dataStore)

        lifecycleScope.launch {
            settingPreferences.getThemeSetting().collect { isDarkModeActive ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        adapter = MovieAdapter()

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvMovie.layoutManager = layoutManager
        binding.rvMovie.setHasFixedSize(true)
        binding.rvMovie.adapter = adapter

        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ResultsItem) {
                startDetailActivity(data)
            }
        })

        mainViewModel.listMovie.observe(this) {
            if (it.isNotEmpty()) {
                adapter.setList(it)
            } else {
                binding.tvErrorMessage.text = getString(R.string.error_loading_data)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                val query = binding.searchView.text.toString()
                binding.searchBar.setText(query)
                mainViewModel.searchMovie(query)
                searchView.hide()
                true
            }
        }

        binding.searchBar.inflateMenu(R.menu.option_menu)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_fav -> {
                    val intentFav = Intent(this, FavoriteActivity::class.java)
                    startActivity(intentFav)
                    true
                }
                R.id.mode -> {
                    val intentMode = Intent(this, SettingActivity::class.java)
                    startActivity(intentMode)
                    true
                }
                else -> false
            }
        }

    }

    private fun startDetailActivity(data: ResultsItem) {
        Intent(this@MainActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_ID, data.id)
            it.putExtra(DetailActivity.EXTRA_TITLE, data.title)
            it.putExtra(DetailActivity.EXTRA_RATING, data.voteAverage)
            it.putExtra(DetailActivity.EXTRA_POSTER, data.posterPath)
            it.putExtra(DetailActivity.EXTRA_OVERVIEW, data.overview)
            startActivity(it)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}