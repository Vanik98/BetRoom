package com.vanik.betroom.ui.movie

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.databinding.ActivityMovieBinding
import com.vanik.betroom.modules.di.actorModule
import com.vanik.betroom.modules.di.movieViewModelModule
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Movie
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin

class MovieActivity : AppCompatActivity() {
    private val movieViewModel: MovieViewModel by viewModel<MovieViewModel>()
    private lateinit var binding: ActivityMovieBinding
    private lateinit var actor: Actor
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
        actor = Json.decodeFromString(actorJson)
        binding.actor = actor
        badCodeSetPetsViews()
        val movieIdsJson = Json.encodeToString(actor.movieIds)
        binding.movieIdsInActorTextView.text = movieIdsJson
    }

    private fun badCodeSetPetsViews() {
        if (actor.pets != null) {
            binding.moviePetLayout1.visibility = View.VISIBLE
            binding.moviePetNameTextView1.text = actor.pets!![0]!!.name
            binding.moviePetAgeTextView1.text = actor.pets!![0]!!.age.toString()
            binding.moviePetIsSmartTextView1.text = "isSmart ? ${actor.pets!![0]!!.isSmart}"
            if (actor.pets!!.size == 2) {
                binding.moviePetLayout2.visibility = View.VISIBLE
                binding.moviePetNameTextView2.text = actor.pets!![1]!!.name
                binding.moviePetAgeTextView2.text = actor.pets!![1]!!.age.toString()
                binding.moviePetIsSmartTextView2.text = "isSmart ? ${actor.pets!![1]!!.isSmart}"
            }
        }
    }

    private fun createMovieFromViews(): Movie? {
        val name = binding.movieNameEditText.text.toString()
        val imdbRate = binding.movieImdbRateEditText.text.toString()
        val id = binding.movieIdEditText.text.toString()
        var movie: Movie? = null
        if (id.isNotEmpty() && name.isNotEmpty() && imdbRate.isNotEmpty()) {
            movie = Movie(id.toInt(), name, imdbRate.toDouble())
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
                if (actor.movieIds == null) {
                    actor.movieIds = arrayListOf()
                }
                (actor.movieIds as ArrayList<Int>).add(movie.id)
                movieViewModel.insertMovie(actor, movie)
                (adapter.movies as ArrayList<Movie>).add(0, movie)
                val movieIdsJson = Json.encodeToString(actor.movieIds)
                binding.movieIdsInActorTextView.text = movieIdsJson
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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