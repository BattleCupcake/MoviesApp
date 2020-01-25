package com.bravemax.moviesapp.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bravemax.moviesapp.R
import com.bravemax.moviesapp.data.api.POSTER_BASE_URL
import com.bravemax.moviesapp.data.pojo.Movie
import com.bravemax.moviesapp.data.repository.NetworkState
import com.bravemax.moviesapp.ui.single_movie_details.SingleActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.networ_state.view.*

class PopularMoviePagedListAdapter(val context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val movieViewType = 1
    private val networkViewType = 2
    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        if (viewType == movieViewType) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            return MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.networ_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == movieViewType) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            networkViewType
        } else {
            movieViewType
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previosState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previosState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie?, context: Context) {
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate

            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(itemView.cv_iv_movie_poster)

            itemView.setOnClickListener {
                val intent = Intent(context, SingleActivity::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {

            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            } else {
                itemView.progress_bar_item.visibility = View.GONE
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            } else if (networkState != null && networkState == NetworkState.END_OF_LIST) {
                itemView.error_msg_item.visibility = View.GONE
                itemView.error_msg_item.text = networkState.msg
            } else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == oldItem
        }
    }
}