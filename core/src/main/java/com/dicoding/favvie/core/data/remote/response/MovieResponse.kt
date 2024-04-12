package com.dicoding.favvie.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem> = listOf(),

	@field:SerializedName("total_results")
	val totalResults: Int? = null
)

data class ResultsItem(

	@field:SerializedName("overview")
	val overview: String = "",

	@field:SerializedName("title")
	val title: String = "",

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Float = 0.0f,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,
)
