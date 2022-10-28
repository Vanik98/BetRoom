package com.vanik.betroom.ui.main

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.entity.Actor
import com.vanik.betroom.entity.Pet
import com.vanik.betroom.ui.movie.MovieActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var actorButton: Button
    private lateinit var roomButton: Button
    private lateinit var sqlLiteButton: Button
    private lateinit var petNameEditText: EditText
    private lateinit var petAgeEditText: EditText
    private lateinit var petCheckBox: CheckBox
    private lateinit var petNameEditText2: EditText
    private lateinit var petAgeEditText2: EditText
    private lateinit var petCheckBox2: CheckBox
    private lateinit var actorAdapter: ActorAdapter
    private lateinit var dialog: Dialog

    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.connectRepository(applicationContext)
        initViews()
        showDbActors()
        chooseRoomOrLite()
        addActor()
        initialiseAdapter()
    }

    private fun initViews() {
        nameEditText = findViewById(R.id.mainNameEditText)
        surnameEditText = findViewById(R.id.mainSurnameEditText)
        actorButton = findViewById(R.id.mainAddActorButton)
        ageEditText = findViewById(R.id.mainAgeEditText)
        roomButton = findViewById(R.id.mainRoomButton)
        sqlLiteButton = findViewById(R.id.mainSqlLiteButton)
        petNameEditText = findViewById(R.id.mainPetNameEditText)
        petAgeEditText = findViewById(R.id.mainPetAgeEditText)
        petCheckBox = findViewById(R.id.mainPetCheckBox)
        petNameEditText2 = findViewById(R.id.mainPetNameEditText2)
        petAgeEditText2 = findViewById(R.id.mainPetAgeEditText2)
        petCheckBox2 = findViewById(R.id.mainPetCheckBox2)
        dialog = Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        dialog.setContentView(R.layout.dialog_layout)
        initialiseAdapter()
    }

    private fun initialiseAdapter() {
        val actorRecyclerView: RecyclerView = findViewById(R.id.mainActorRecyclerView)
        actorRecyclerView.layoutManager = LinearLayoutManager(this)
        actorRecyclerView.adapter = ActorAdapter(mainViewModel.actors) {
            openMovieActivity(it)
        }
        actorAdapter = actorRecyclerView.adapter as ActorAdapter
    }

    private fun createPetsFromViews(): List<Pet>? {
        var pets: List<Pet>? = null
        val petName = petNameEditText.text.toString()
        val petAge = petAgeEditText.text.toString()
        val petName2 = petNameEditText2.text.toString()
        val petAge2 = petAgeEditText2.text.toString()
        if (petName.isNotEmpty() && petAge.isNotEmpty()) {
            val pet1 = Pet(0, petName, petAge.toInt(), petCheckBox.isChecked)
            pets = arrayListOf()
            pets.add(pet1)
            if ((petName2.isNotEmpty() && petAge.isEmpty()) || (petName2.isEmpty() && petAge.isNotEmpty())) {
                showToast("fill all fields")
            } else if (petName2.isNotEmpty() || petAge2.isNotEmpty()) {
                val pet2 = Pet(0, petName2, petAge2.toInt(), petCheckBox2.isChecked)
                pets.add(pet2)
            }
        } else if (petName.isNotEmpty() || petAge.isNotEmpty()) {
            showToast("fill all fields")
        }
        return pets
    }

    private fun createActorFromViews(): Actor? {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val age = ageEditText.text.toString()
        return if (name.isNotEmpty() && surname.isNotEmpty() && age.isNotEmpty()) {
            Actor(name, surname, age.toInt(), createPetsFromViews(), arrayListOf())
        } else {
            showToast("fill all fields")
            null
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun chooseRoomOrLite() {
        roomButton.setOnClickListener {
            sqlLiteButton.isEnabled = true
            roomButton.isEnabled = false
            mainViewModel.isRoom = true
            mainViewModel.actors.clear()
            mainViewModel.actors.addAll(mainViewModel.actorsRoom)
            actorAdapter.notifyDataSetChanged()
        }
        sqlLiteButton.setOnClickListener {
            roomButton.isEnabled = true
            sqlLiteButton.isEnabled = false
            mainViewModel.isRoom = false
            mainViewModel.actors.clear()
            mainViewModel.actors.addAll(mainViewModel.actorsLite)
            if (mainViewModel.actorsLite.isNotEmpty()) {
                actorAdapter.notifyDataSetChanged()
            } else {
                showLiteActors()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addActor() {
        actorButton.setOnClickListener {
            val actor = createActorFromViews()
            if (actor != null) {
                mainViewModel.insertActor(actor)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRoomActors() {
        dialog.show()
        mainViewModel.getAllRoomActors().observe(this) {
            mainViewModel.actors.clear()
            mainViewModel.actorsRoom.clear()
            for (i in it.indices) {
                mainViewModel.actors.add(it[it.size - 1 - i])
            }
            mainViewModel.actorsRoom.addAll(mainViewModel.actors)
            actorAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showLiteActors() {
        dialog.show()
        mainViewModel.getAllLiteActors().observe(this) {
            mainViewModel.actors.clear()
            mainViewModel.actorsLite.clear()
            for (i in it.indices) {
                mainViewModel.actors.add(it[it.size - 1 - i])
            }
            mainViewModel.actorsLite.addAll(mainViewModel.actors)
            actorAdapter.notifyDataSetChanged()
            dialog.dismiss()
        }
    }

    private fun showDbActors() {
        when (mainViewModel.isRoom) {
            true -> showRoomActors()
            false -> showLiteActors()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun openMovieActivity(actor: Actor) {
        val intent = Intent(this, MovieActivity::class.java)
        intent.putExtra("isRoom", mainViewModel.isRoom)
        intent.putExtra("actor", Json.encodeToString(actor))
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}