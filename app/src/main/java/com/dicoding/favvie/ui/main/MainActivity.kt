package com.dicoding.favvie.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favvie.R
import com.dicoding.favvie.data.response.ResultsItem
import com.dicoding.favvie.databinding.ActivityMainBinding
import com.dicoding.favvie.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Intent(this@MainActivity, DetailActivity::class.java).also {
                    it.putExtra(DetailActivity.EXTRA_ID, data.id)
                    it.putExtra(Intent.EXTRA_TITLE, data.title)
                    startActivity(it)
                }
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

//        binding.searchBar.inflateMenu(R.menu.option_menu)
//        binding.searchBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.list_fav -> {
//                    val intent
//                }
//            }
//        }

    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}