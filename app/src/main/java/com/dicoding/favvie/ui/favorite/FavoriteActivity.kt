package com.dicoding.favvie.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.favvie.R
import com.dicoding.favvie.data.local.entity.FavoriteEntity
import com.dicoding.favvie.databinding.ActivityFavoriteBinding
import com.dicoding.favvie.ui.ViewModelFactory
import com.dicoding.favvie.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val dataDeleted = result.data?.getBooleanExtra("data_deleted", false) ?: false
            if (dataDeleted) {
                favoriteViewModel.getAllFavorites().observe(this) {
                    setFavorite(it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite_movie)

        favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllFavorites().observe(this) { list ->
            if (list.isNotEmpty()) {
                setFavorite(list)
            } else {
                binding.tvErrorMessage.text =
                    getString(R.string.gagal_memuat_data_silakan_coba_kembali)
            }
        }

        favoriteViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteViewModel::class.java]
    }

    private fun setFavorite(favoriteEntities: List<FavoriteEntity.Item>) {
        val items = arrayListOf<FavoriteEntity.Item>()
        favoriteEntities.map {
            val item = FavoriteEntity.Item(
                id = it.id,
                title = it.title,
                overview = it.overview,
                voteAverage = it.voteAverage,
                posterPath = it.posterPath,
            )
            items.add(item)
        }
        setupRecyclerView(items)
    }

    private fun setupRecyclerView(items: ArrayList<FavoriteEntity.Item>) {
        val adapter = FavoriteAdapter(items)
        binding.rvFavoriteUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavoriteUser.setHasFixedSize(true)
        binding.rvFavoriteUser.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteEntity.Item) {
                startDetailActivity(data)
            }
        })
    }

    private fun startDetailActivity(data: FavoriteEntity.Item) {
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_ID, data.id)
        intent.putExtra(DetailActivity.EXTRA_TITLE, data.title)
        intent.putExtra(DetailActivity.EXTRA_RATING, data.voteAverage)
        intent.putExtra(DetailActivity.EXTRA_OVERVIEW, data.overview)
        intent.putExtra(DetailActivity.EXTRA_POSTER, data.posterPath)
        resultLauncher.launch(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}