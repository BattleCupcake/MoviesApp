package com.bravemax.moviesapp.ui.singleMovieDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bravemax.moviesapp.R
import com.bravemax.moviesapp.data.api.IMovieDB
import com.bravemax.moviesapp.data.api.MovieDBClient
import com.bravemax.moviesapp.data.api.POSTER_BASE_URL
import com.bravemax.moviesapp.data.pojo.MovieDetails
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single.*
import java.text.NumberFormat
import java.util.*

class SingleActivity : AppCompatActivity() {

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var movieRepository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        val movieId: Int = intent.getIntExtra("id", 1)

        val apiService: IMovieDB = MovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel = getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })
    }

    fun bindUI(it: MovieDetails) {
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + " minutes"
        movie_overview.text = it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterURL = POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterURL)
            .into(iv_movie_poster)
    }

    @Suppress("UNCHECKED_CAST")
    private fun getViewModel(movieId: Int): SingleMovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(movieRepository, movieId) as T
            }
        })[SingleMovieViewModel::class.java]
    }
}
