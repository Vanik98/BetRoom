package com.vanik.betroom.ui.movie

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.db.repository.Repository
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Movie
import com.vanik.betroom.ui.main.ActorAdapter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.w3c.dom.Text

class MovieActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var surnameTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var petNameTextView: TextView
    private lateinit var petAgeTextView: TextView
    private lateinit var petIsSmart: TextView
    private lateinit var petNameTextView2: TextView
    private lateinit var petAgeTextView2: TextView
    private lateinit var petIsSmart2: TextView
    private lateinit var petLayout1: LinearLayoutCompat
    private lateinit var petLayout2: LinearLayoutCompat
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieNameEditText: EditText
    private lateinit var imdbRateEditText: EditText
    private lateinit var addMovieButton: Button
    private lateinit var idEditText: EditText
    private lateinit var movieIdsTextView: TextView
    private lateinit var actor: Actor
    private lateinit var adapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        movieViewModel = ViewModelProvider(this)[MovieViewModel::class.java]
        initViews()
        showActor()
        showDbMovies()
        addMovie()
    }

    private fun initViews() {
        nameTextView = findViewById(R.id.movieActorNameTextView)
        surnameTextView = findViewById(R.id.movieActorSurnameTextView)
        ageTextView = findViewById(R.id.movieActorAgeTextView)
        petNameTextView = findViewById(R.id.moviePetNameTextView1)
        petAgeTextView = findViewById(R.id.moviePetAgeTextView1)
        petIsSmart = findViewById(R.id.moviePetIsSmartTextView1)
        petNameTextView2 = findViewById(R.id.moviePetNameTextView2)
        petAgeTextView2 = findViewById(R.id.moviePetAgeTextView2)
        petIsSmart2 = findViewById(R.id.moviePetIsSmartTextView2)
        petLayout1 = findViewById(R.id.moviePetLayout1)
        petLayout2 = findViewById(R.id.moviePetLayout2)
        movieNameEditText = findViewById(R.id.movieNameEditText)
        imdbRateEditText = findViewById(R.id.movieImdbRateEditText)
        addMovieButton = findViewById(R.id.movieAddButton)
        idEditText = findViewById(R.id.movieIdEditText)
        movieIdsTextView = findViewById(R.id.movieIdsInActorTextView)
        initialiseAdapter()

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
        nameTextView.text = actor.name
        surnameTextView.text = actor.surname
        ageTextView.text = actor.age.toString()
        if (actor.pets != null) {
            petLayout1.visibility = View.VISIBLE
            petNameTextView.text = actor.pets!![0]!!.name.toString()
            petAgeTextView.text = actor.pets!![0]!!.age.toString()
            petIsSmart.text = "isSmart ? ${actor.pets!![0]!!.isSmart}"
            if (actor.pets!!.size == 2) {
                petLayout2.visibility = View.VISIBLE
                petNameTextView2.text = actor.pets!![1]!!.name
                petAgeTextView.text = actor.pets!![1]!!.age.toString()
                petIsSmart2.text = "isSmart ? ${actor.pets!![1]!!.isSmart}"
            }
        }
        val movieIdsJson = Json.encodeToString(actor.movieIds)
        movieIdsTextView.text = movieIdsJson
    }

    private fun createMovieFromViews(): Movie? {
        val name = movieNameEditText.text.toString()
        val imdbRate = imdbRateEditText.text.toString()
        val id = idEditText.text.toString()
        var movie: Movie? = null
        if (id.isNotEmpty() && name.isNotEmpty() && imdbRate.isNotEmpty()) {
            movie = Movie(id.toInt(), name, imdbRate.toDouble())
        }else {
            showToast("fill all fields")
        }
        return movie
    }

    private fun addMovie() {
        addMovieButton.setOnClickListener {
            val movie = createMovieFromViews()
            if (movie != null) {
                if (actor.movieIds == null) {
                    actor.movieIds = arrayListOf()
                }
                (actor.movieIds as ArrayList<Int>).add(movie.id)
                movieViewModel.insertMovie(actor, movie)
                (adapter.movies as ArrayList<Movie>).add(movie)
                val movieIdsJson = Json.encodeToString(actor.movieIds)
                movieIdsTextView.text = movieIdsJson
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDbMovies() {
        movieViewModel.isRoom = intent.getBooleanExtra("isRoom", true)
        movieViewModel.getMovies().observe(this) {
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