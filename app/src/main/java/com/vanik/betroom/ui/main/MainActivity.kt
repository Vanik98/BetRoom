package com.vanik.betroom.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.databinding.ActivityMainBinding
import com.vanik.betroom.proxy.model.Actor
import com.vanik.betroom.proxy.model.Pet
import com.vanik.betroom.ui.movie.MovieActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actorAdapter: ActorAdapter
    private lateinit var dialog: Dialog
    private var isRoom = true
    private val mainViewModel: MainViewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel.fetchData()
        setupViews()
        showDbActors()
        chooseRoomOrLite()
        addActor()
    }

    private fun setupViews() {
        initDialog()
        initAdapter()
    }

    private fun initDialog() {
        dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_layout)
    }

    private fun initAdapter() {
        val actorRecyclerView: RecyclerView = findViewById(R.id.mainActorRecyclerView)
        actorRecyclerView.layoutManager = LinearLayoutManager(this)
        actorRecyclerView.adapter = ActorAdapter(mainViewModel.actors) { openMovieActivity(it) }
        actorAdapter = actorRecyclerView.adapter as ActorAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun chooseRoomOrLite() {
        binding.mainRoomButton.setOnClickListener {
            isRoom = true
            binding.mainRoomButton.isEnabled = !isRoom
            binding.mainSqlLiteButton.isEnabled = isRoom
            mainViewModel.chooseDb(isRoom)
            actorAdapter.notifyDataSetChanged()
        }
        binding.mainSqlLiteButton.setOnClickListener {
            isRoom = false
            binding.mainRoomButton.isEnabled = !isRoom
            binding.mainSqlLiteButton.isEnabled = isRoom
            mainViewModel.chooseDb(isRoom)
            actorAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showDbActors() {
        dialog.show()
        mainViewModel.actorsLiveData.observe(this) {
            dialog.dismiss()
            actorAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addActor() {
        binding.mainAddActorButton.setOnClickListener {
            val actor = createActorFromViews()
            if (actor != null) {
                mainViewModel.insertActor(isRoom,actor)
                if(!mainViewModel.insertOk){
                    showToast("this name is already in the database")
                }
            }
        }
    }

    private fun createActorFromViews(): Actor? {
        val name = binding.mainNameEditText.text.toString()
        val surname = binding.mainSurnameEditText.text.toString()
        val age = binding.mainAgeEditText.text.toString()
        return if (name.isNotEmpty() && surname.isNotEmpty() && age.isNotEmpty()) {
            Actor(name, surname, age.toInt(), createPetsFromViews(), arrayListOf())
        } else {
            showToast("fill all fields")
            null
        }
    }

    private fun createPetsFromViews(): List<Pet>? {
        var pets: List<Pet>? = null
        val petName = binding.mainPetNameEditText.text.toString()
        val petAge = binding.mainPetAgeEditText.text.toString()
        val petName2 = binding.mainPetNameEditText2.text.toString()
        val petAge2 = binding.mainPetAgeEditText2.text.toString()
        if (petName.isNotEmpty() && petAge.isNotEmpty()) {
            val pet1 = Pet(0, petName, petAge.toInt(), binding.mainPetCheckBox.isChecked)
            pets = arrayListOf()
            pets.add(pet1)
            if ((petName2.isNotEmpty() && petAge2.isNotEmpty())) {
                val pet2 = Pet(0, petName2, petAge2.toInt(), binding.mainPetCheckBox2.isChecked)
                pets.add(pet2)
            }
        }
        return pets
    }

    @SuppressLint("SuspiciousIndentation")
    private fun openMovieActivity(actor: Actor) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra("isRoom", isRoom)
        intent.putExtra("actor", Json.encodeToString(actor))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}