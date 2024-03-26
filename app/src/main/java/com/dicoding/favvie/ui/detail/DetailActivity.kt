package com.dicoding.favvie.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.favvie.R
import com.dicoding.favvie.data.local.entity.FavoriteEntity
import com.dicoding.favvie.databinding.ActivityDetailBinding
import com.dicoding.favvie.ui.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_movie)

        val movieId = intent.getIntExtra(EXTRA_ID, 0)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        val overview = intent.getStringExtra(EXTRA_OVERVIEW) ?: ""
        val posterPath = intent.getStringExtra(EXTRA_POSTER) ?: ""
        val voteAverage = intent.getFloatExtra(EXTRA_RATING, 0F)
        Bundle().putInt(EXTRA_ID, movieId)

        detailViewModel.getDetailMovie(movieId)
        showLoading(true)

        detailViewModel.detailMovie.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    val baseUrl = "https://image.tmdb.org/t/p/w500"
                    Glide.with(this@DetailActivity)
                        .load(baseUrl + it.posterPath)
                        .apply{
                            into(imgDetailPoster)
                            into(imgPosterBanner)
                        }
                    tvDetailTitle.text = it.title
                    tvDetailRating.text = String.format("%.1f", it.voteAverage)
                    tvRatingCount.text = it.voteCount.toString()
                    tvDetailSinopsis.text = it.overview
                    val genres = it.genres?.joinToString(", ") { genresItem ->
                        genresItem?.name.orEmpty()
                    }
                    tvDetailGenre.text = genres
                }
            } else {
                detailViewModel.toastMessage.observe(this) { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.getDataById(movieId).observe(this) {
            isFavorite = it.isNotEmpty()
            val favorite = FavoriteEntity.Item(movieId, title, overview, voteAverage, posterPath)
            if (isFavorite) {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite
                    )
                )
                binding.fabFavorite.contentDescription = getString(R.string.favorite_removed)
            } else {
                binding.fabFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabFavorite.context,
                        R.drawable.ic_favorite_border
                    )
                )
                binding.fabFavorite.contentDescription = getString(R.string.added_to_favorite)
            }

            binding.fabFavorite.setOnClickListener {
                if (isFavorite) {
                    detailViewModel.delete(favorite)
                    val resultIntent = Intent()
                    resultIntent.putExtra("data_deleted", true)
                    setResult(RESULT_OK, resultIntent)
                    Toast.makeText(this, getString(R.string.favorite_removed), Toast.LENGTH_SHORT).show()
                } else {
                    detailViewModel.insert(favorite)
                    Log.d("Detail Activity", "insert $favorite")
                    Toast.makeText(this, getString(R.string.added_to_favorite), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_OVERVIEW = "extra_overview"
        const val EXTRA_POSTER = "extra_poster"
        const val EXTRA_RATING = "extra_rating"
    }
}