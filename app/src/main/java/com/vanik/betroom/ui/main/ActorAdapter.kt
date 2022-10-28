package com.vanik.betroom.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.R
import com.vanik.betroom.entity.Actor

class ActorAdapter(var actors: List<Actor>, val onClick:(actor:Actor)->Unit) : RecyclerView.Adapter<ActorAdapter.ActorHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorHolder {
        return ActorHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_actor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorHolder, position: Int) {
        holder.bind(actors[position])
       // holder.setOnClickListener(actors[position])
    }

    override fun getItemCount() = actors.size

    inner class ActorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.actorItemNameTextView)
        private val surname: TextView = itemView.findViewById(R.id.actorItemSurnameTextView)
        private val petLayout1: LinearLayoutCompat = itemView.findViewById(R.id.actorItemPetLayout1)
        private val petLayout2: LinearLayoutCompat = itemView.findViewById(R.id.actorItemPetLayout2)
        private val petName1: TextView = itemView.findViewById(R.id.actorItemPetNameTextView1)
        private val petAge1: TextView = itemView.findViewById(R.id.actorItemPetAgeTextView1)
        private val isSmart1: TextView = itemView.findViewById(R.id.actorItemPetIsSmartTextView1)
        private val petName2: TextView = itemView.findViewById(R.id.actorItemPetNameTextView2)
        private val petAge2: TextView = itemView.findViewById(R.id.actorItemPetAgeTextView2)
        private val isSmart2: TextView = itemView.findViewById(R.id.actorItemPetIsSmartTextView2)

        fun setOnClickListener(actor: Actor) {
            itemView.setOnClickListener {
                onClick.invoke(actor)
            }
        }
        init {
           itemView.setOnClickListener { onClick.invoke(actors[adapterPosition]) }
        }

        @SuppressLint("SetTextI18n")
        fun bind(actor: Actor) {
            name.text = actor.name
            surname.text = actor.surname
            if (actor.pets != null) {
                petLayout1.visibility = View.VISIBLE
                petName1.text = actor.pets[0]!!.name
                petAge1.text = actor.pets[0]!!.age.toString()
                isSmart1.text = "isSmart ? ${actor.pets[0]!!.isSmart}"
                if (actor.pets.size == 2) {
                    petLayout2.visibility = View.VISIBLE
                    petName2.text = actor.pets[1]!!.name
                    petAge2.text = actor.pets[1]!!.age.toString()
                    isSmart2.text = "isSmart ? ${actor.pets[1]!!.isSmart}"
                }
            }
        }

    }


}