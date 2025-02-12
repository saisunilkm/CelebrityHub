package com.example.celebrityhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PeopleAdapter (private val onClick: (Person) -> Unit) :
    RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>(){
    private val peopleList = mutableListOf<Person>()

    fun submitList(list: List<Person>) {
        peopleList.clear()
        peopleList.addAll(list)
        notifyDataSetChanged()
    }

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(person: Person, onClick: (Person) -> Unit) {
            val name = itemView.findViewById<TextView>(R.id.name)
            val image = itemView.findViewById<ImageView>(R.id.image)

            name.text = person.name
            Glide.with(itemView).load("https://image.tmdb.org/t/p/w500" + person.profilePath).into(image)

            itemView.setOnClickListener { onClick(person) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(peopleList[position], onClick)
    }

    override fun getItemCount(): Int = peopleList.size
}