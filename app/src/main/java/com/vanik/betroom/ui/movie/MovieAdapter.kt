package com.vanik.betroom.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.entity.Movie

class MovieAdapter(var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val id: TextView = itemView.findViewById(R.id.movieItemIdTextView)
        private val name: TextView = itemView.findViewById(R.id.movieItemNameTextView)
        private val imdbRate: TextView = itemView.findViewById(R.id.movieItemImdbRateTextView)
        fun bind(movie: Movie) {
            id.text = movie.id.toString()
            name.text = movie.name
            imdbRate.text = movie.imdbRate.toString()
        }
    }

}