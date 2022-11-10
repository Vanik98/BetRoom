package com.vanik.betroom.ui.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.databinding.ItemMovieBinding
import com.vanik.betroom.data.model.Movie

class MovieAdapter(var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private lateinit var binding: ItemMovieBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)
        return MovieHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) = holder.bind(movies[position])

    override fun getItemCount() = movies.size

    inner class MovieHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
        }
    }
}