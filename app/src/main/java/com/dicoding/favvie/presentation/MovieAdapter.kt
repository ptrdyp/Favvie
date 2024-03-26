package com.dicoding.favvie.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.favvie.core.data.remote.response.ResultsItem
import com.dicoding.favvie.databinding.ItemRowMovieBinding

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private val listMovie = ArrayList<ResultsItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(data: ResultsItem)
    }

    inner class MyViewHolder(private val binding: ItemRowMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: ResultsItem) {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            binding.apply {
                Glide.with(imgItemPoster.context)
                    .load(baseUrl + movie.posterPath)
                    .into(imgItemPoster)
                tvItemTitle.text = movie.title
                tvItemRating.text = String.format("%.1f", movie.voteAverage)
                tvItemSinopsis.text = movie.overview
            }

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(movie)
            }
        }
    }

    fun setList(newList: List<ResultsItem>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallback(listMovie, newList))
        listMovie.clear()
        listMovie.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = listMovie[position]
        holder.bind(movie)
    }

    class MainDiffCallback(
        private val oldList: List<ResultsItem>,
        private val newList: List<ResultsItem>
    ) : DiffUtil.Callback() {
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