package com.vanik.betroom.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vanik.betroom.databinding.ItemActorBinding
import com.vanik.growdb.model.Actor

class ActorAdapter(
    var actors: List<Actor>,
    val onClick: (actor: Actor) -> Unit
) : RecyclerView.Adapter<ActorAdapter.ActorHolder>() {

    private lateinit var binding: ItemActorBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorHolder {
        binding = ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorHolder, position: Int) = holder.bind(actors[position])

    override fun getItemCount() = actors.size

    inner class ActorHolder(private val binding: ItemActorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(actor: Actor) {
            binding.actor = actor
            setPetsInViews(actor)
            binding.root.setOnClickListener { onClick.invoke(actor) }
        }

        @SuppressLint("SetTextI18n")
        private fun setPetsInViews(actor: Actor) {
            binding.actorItemPetLayout1.visibility = View.GONE
            binding.actorItemPetLayout2.visibility = View.GONE
            if (actor.pets != null) {
                binding.actorItemPetLayout1.visibility = View.VISIBLE
                binding.actorItemPetNameTextView1.text = actor.pets!![0]!!.name
                binding.actorItemPetAgeTextView1.text = actor.pets!![0]!!.age.toString()
                binding.actorItemPetIsSmartTextView1.text = "isSmart ? ${actor.pets!![0]!!.isSmart}"
                if (actor.pets!!.size == 2) {
                    binding.actorItemPetLayout2.visibility = View.VISIBLE
                    binding.actorItemPetNameTextView2.text = actor.pets!![1]!!.name
                    binding.actorItemPetAgeTextView2.text = actor.pets!![1]!!.age.toString()
                    binding.actorItemPetIsSmartTextView2.text =
                        "isSmart ? ${actor.pets!![1]!!.isSmart}"
                }
            }
        }
    }
}