package com.bravemax.moviesapp.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.bravemax.moviesapp.data.api.IMovieDB
import com.bravemax.moviesapp.data.pojo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory(
    private val apiService: IMovieDB,
    private val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, Movie>() {

    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return movieDataSource
    }
}