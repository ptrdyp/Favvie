package com.dicoding.favvie.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favvie.core.data.Resource
import com.dicoding.favvie.R
import com.dicoding.favvie.databinding.ActivityMainBinding
import com.dicoding.favvie.core.ui.MovieAdapter
import com.dicoding.favvie.setting.SettingActivity
import com.dicoding.favvie.core.data.local.room.SettingPreferences
import com.dicoding.favvie.core.data.local.room.dataStore
import com.dicoding.favvie.detail.DetailActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private val mainViewModel: MainViewModel by viewModel()

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

        movieAdapter = MovieAdapter()

        movieAdapter.onItemClick = { selectedData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, selectedData)
            startActivity(intent)
        }

        supportActionBar?.hide()

        mainViewModel.movie.observe(this) { movie ->
            if (movie != null) {
                when (movie) {
                    is Resource.Loading<*> -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Success<*> -> {
                        binding.progressBar.visibility = View.GONE
                        movie.data?.let { movieAdapter.setList(it) }
                    }
                    is Resource.Error<*> -> {
                        with(binding) {
                            progressBar.visibility = View.GONE
                            viewError.root.visibility = View.VISIBLE
                            viewError.tvError.text = movie.message ?: getString(R.string.oops_something_went_wrong)
                        }
                    }
                }
            }
        }

        with(binding.rvMovie) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                val query = binding.searchView.text.toString()
                binding.searchBar.setText(query)
                mainViewModel.searchMovie(query).observe(this@MainActivity) { movies ->
                    movieAdapter.setList(movies)
                }
                searchView.hide()
                true
            }
        }

        binding.searchBar.inflateMenu(R.menu.option_menu)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_fav -> {
                    val intentFav = (Intent(this, Class.forName("com.dicoding.favvie.favorite.FavoriteActivity")))
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
}