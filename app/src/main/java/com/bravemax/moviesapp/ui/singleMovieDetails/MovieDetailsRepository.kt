package com.bravemax.moviesapp.ui.singleMovieDetails

import androidx.lifecycle.LiveData
import com.bravemax.moviesapp.data.api.IMovieDB
import com.bravemax.moviesapp.data.pojo.MovieDetails
import com.bravemax.moviesapp.data.repository.MovieDetailsDataSource
import com.bravemax.moviesapp.data.repository.NetworkState
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: IMovieDB) {

    lateinit var movieDetailsDataSource: MovieDetailsDataSource

    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {
        movieDetailsDataSource = MovieDetailsDataSource(apiService, compositeDisposable)
        movieDetailsDataSource.fetchMovieDetails(movieId)
        return movieDetailsDataSource.mDownloadedMovieResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsDataSource.mNetworkState
    }
}