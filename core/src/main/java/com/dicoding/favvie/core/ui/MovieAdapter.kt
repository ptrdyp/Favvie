package com.dicoding.favvie.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.core.R
import com.dicoding.core.databinding.ItemRowMovieBinding
import com.dicoding.favvie.core.domain.model.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private val listMovie = ArrayList<Movie>()
    var onItemClick: ((Movie) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowMovieBinding.bind(itemView)
        fun bind(movie: Movie) {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            binding.apply {
                Glide.with(imgItemPoster.context)
                    .load(baseUrl + movie.posterPath)
                    .into(imgItemPoster)
                tvItemTitle.text = movie.title
                tvItemRating.text = String.format("%.1f", movie.voteAverage)
                tvItemSinopsis.text = movie.overview
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(listMovie[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_movie, parent, false)
        )

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    fun setList(newList: List<Movie>) {
        val diffResult = DiffUtil.calculateDiff(MovieDiffCallback(listMovie, newList))
        listMovie.clear()
        listMovie.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class MovieDiffCallback(private val oldList: List<Movie>, private val newList: List<Movie>) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}