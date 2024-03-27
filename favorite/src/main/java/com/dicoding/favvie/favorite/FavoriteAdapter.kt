package com.dicoding.favvie.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.core.data.local.entity.FavoriteEntity
import com.dicoding.core.databinding.ItemRowMovieBinding
import com.dicoding.favvie.ui.detail.DetailActivity

class FavoriteAdapter(
    private val listFavorite: MutableList<FavoriteEntity.Item>
) : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteEntity.Item)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class MyViewHolder(private val binding: ItemRowMovieBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(movie: FavoriteEntity.Item) {
            binding.apply {
                val baseUrl = "https://image.tmdb.org/t/p/w500"
                Glide.with(itemView.context)
                    .load(baseUrl + movie.posterPath)
                    .into(imgItemPoster)
                tvItemTitle.text = movie.title
                tvItemRating.text = String.format("%.1f", movie.voteAverage)
                tvItemSinopsis.text = movie.overview
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movies = listFavorite[position]
        holder.bind(movies)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_ID, movies.id)
            onItemClickCallback.onItemClicked(movies)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listFavorite.size
}