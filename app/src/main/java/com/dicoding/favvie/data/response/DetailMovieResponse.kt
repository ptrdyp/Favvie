package com.dicoding.favvie.data.response

import com.google.gson.annotations.SerializedName

data class DetailMovieResponse(

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("imdb_id")
	val imdbId: String? = null,

	@field:SerializedName("video")
	val video: Boolean? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("revenue")
	val revenue: Int? = null,

	@field:SerializedName("genres")
	val genres: List<GenresItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Any? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class GenresItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

