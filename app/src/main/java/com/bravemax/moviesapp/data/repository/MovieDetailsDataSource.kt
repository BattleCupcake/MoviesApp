package com.bravemax.moviesapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bravemax.moviesapp.data.api.IMovieDB
import com.bravemax.moviesapp.data.pojo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDetailsDataSource(
    private val apiService: IMovieDB,
    private val compositeDisposable: CompositeDisposable
) {

    private val networkState = MutableLiveData<NetworkState>()
    val mNetworkState: LiveData<NetworkState>
        get() = networkState

    private val downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val mDownloadedMovieResponse: LiveData<MovieDetails>
        get() = downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {
        networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                            downloadedMovieDetailsResponse.postValue(it)
                            networkState.postValue(NetworkState.LOADED)
                        },
                        {
                            networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }
                    )
            )
        } catch (e: Exception) {
            Log.e("MovieDetailsDataSource", e.message)
        }
    }
}