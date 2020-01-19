package com.bravemax.moviesapp.data.api

import com.bravemax.moviesapp.data.pojo.MovieDetails
import com.bravemax.moviesapp.data.pojo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieDB {

//    Example request
//https://api.themoviedb.org/3/movie/popular?api_key=cbc3cbfa98d5e822e74d6d0b419ba32c
// https://api.themoviedb.org/3/movie/419704?api_key=cbc3cbfa98d5e822e74d6d0b419ba32c

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

}