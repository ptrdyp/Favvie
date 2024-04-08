package com.dicoding.favvie.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dicoding.core.R
import com.dicoding.favvie.core.domain.model.Movie
import com.dicoding.favvie.databinding.ActivityDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_movie)

        val detailMovie = intent.getParcelableExtra<Movie>(EXTRA_DATA)

        binding.apply {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(this@DetailActivity)
                .load(baseUrl + detailMovie?.posterPath)
                .apply {
                    into(imgDetailPoster)
                    into(imgPosterBanner)
                }
            tvDetailTitle.text = detailMovie?.title
            tvDetailRating.text = String.format("%.1f", detailMovie?.voteAverage)
            tvRatingCount.text = detailMovie?.voteCount.toString()
            tvDetailSinopsis.text = detailMovie?.overview
        }

        var statusFavorite = detailMovie?.isFavorite
        if (statusFavorite != null) {
            setStatusFavorite(statusFavorite)
            binding.fabFavorite.setOnClickListener {
                statusFavorite = !statusFavorite!!
                if (detailMovie != null) {
                    detailViewModel.setFavoriteMovie(detailMovie, statusFavorite!!)
                }
                setStatusFavorite(statusFavorite!!)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
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
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }
}