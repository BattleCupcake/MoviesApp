package com.bravemax.moviesapp.ui.single_movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bravemax.moviesapp.data.pojo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(
    private val movieRepository: MovieDetailsRepository,
    movieId: Int
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val movieDetails: LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable, movieId)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}