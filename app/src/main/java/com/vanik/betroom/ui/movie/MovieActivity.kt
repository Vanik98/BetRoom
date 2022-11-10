package com.vanik.betroom.ui.movie

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.databinding.ActivityMovieBinding
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModel<MovieViewModel>()
    private lateinit var binding: ActivityMovieBinding
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie)
        initialiseAdapter()
        showActor()
        lifecycleScope.launch { showDbMovies() }
        addMovie()
    }

    private fun initialiseAdapter() {
        val actorRecyclerView: RecyclerView = findViewById(R.id.movieRecyclerView)
        actorRecyclerView.layoutManager = LinearLayoutManager(this)
        actorRecyclerView.adapter = MovieAdapter(movieViewModel.movies)
        adapter = actorRecyclerView.adapter as MovieAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun showActor() {
        val actorJson: String = intent.getStringExtra("actor").toString()
        movieViewModel.actor = Json.decodeFromString(actorJson)
        binding.actor = movieViewModel.actor
        badCodeSetPetsViews()
        val movieIdsJson = Json.encodeToString(movieViewModel.actor.movieIds)
        binding.movieIdsInActorTextView.text = movieIdsJson
    }

    private fun badCodeSetPetsViews() {
        if (movieViewModel.actor.pets != null) {
            binding.moviePetLayout1.visibility = View.VISIBLE
            binding.moviePetNameTextView1.text = movieViewModel.actor.pets!![0]!!.name
            binding.moviePetAgeTextView1.text = movieViewModel.actor.pets!![0]!!.age.toString()
            binding.moviePetIsSmartTextView1.text = "isSmart ? ${movieViewModel.actor.pets!![0]!!.isSmart}"
            if (movieViewModel.actor.pets!!.size == 2) {
                binding.moviePetLayout2.visibility = View.VISIBLE
                binding.moviePetNameTextView2.text = movieViewModel.actor.pets!![1]!!.name
                binding.moviePetAgeTextView2.text = movieViewModel.actor.pets!![1]!!.age.toString()
                binding.moviePetIsSmartTextView2.text = "isSmart ? ${movieViewModel.actor.pets!![1]!!.isSmart}"
            }
        }
    }

    private fun createMovieFromViews(): Movie? {
        val name = binding.movieNameEditText.text.toString()
        val imdbRate = binding.movieImdbRateEditText.text.toString()
        val id = binding.movieIdEditText.text.toString()
        var movie: Movie? = null
        if (id.isNotEmpty() && name.isNotEmpty() && imdbRate.isNotEmpty()) {
            movie = Movie(id.toInt(), name, imdbRate.toDouble(),movieViewModel.actor.name)
        } else {
            showToast("fill all fields")
        }
        return movie
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMovie() {
        binding.movieAddButton.setOnClickListener {
            val movie = createMovieFromViews()
            if (movie != null) {
                movieViewModel.insertMovie(movieViewModel.actor, movie)
                val movieIdsJson = Json.encodeToString(movieViewModel.actor.movieIds)
                binding.movieIdsInActorTextView.text = movieIdsJson
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
    suspend fun showDbMovies() {
        movieViewModel.isRoom = intent.getBooleanExtra("isRoom", true)
            movieViewModel.getMovies().flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collect  {
                movieViewModel.movies.clear()
                for (i in it.indices) {
                    movieViewModel.movies.add(it[it.size - 1 - i])
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}