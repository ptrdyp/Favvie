package com.dicoding.favvie.ui.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dicoding.favvie.R
import com.dicoding.favvie.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail_movie)

        val movieId = intent.getIntExtra(EXTRA_ID, 0)
        detailViewModel.getDetailMovie(movieId)

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
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}