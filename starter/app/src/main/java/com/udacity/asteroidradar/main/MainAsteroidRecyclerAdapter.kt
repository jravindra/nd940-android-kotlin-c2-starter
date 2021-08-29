package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class MainAsteroidRecyclerAdapter(private val listener: AsteroidClickListener) :
    ListAdapter<Asteroid, ItemViewHolder>(AsteroidCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        val asteroid = getItem(position)
        viewHolder.bind(asteroid, listener)
    }
}

class ItemViewHolder(private val binding: ListItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(asteroid: Asteroid, listener: AsteroidClickListener) {
        binding.asteroid = asteroid
        binding.asteroidItem.setOnClickListener {
            listener.onClick(asteroid)
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }

}

class AsteroidCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(asteroid1: Asteroid, asteroid2: Asteroid): Boolean {
        return asteroid1.id == asteroid2.id
    }

    override fun areContentsTheSame(asteroid1: Asteroid, asteroid2: Asteroid): Boolean {
        return asteroid1 == asteroid2
    }

}

class AsteroidClickListener(private val onClickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = onClickListener(asteroid)
}


