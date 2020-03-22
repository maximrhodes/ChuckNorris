package com.example.chucknorris

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JokeAdapter  : RecyclerView.Adapter<JokeAdapter.JokeViewHolder>(){

    private var jokeList: List<Joke> = Jokes.jokes_string_list.stringToJoke()
        set(newJokeList)
        {
            field = newJokeList
            notifyDataSetChanged()
        }

    class JokeViewHolder(val joke: TextView) : RecyclerView.ViewHolder(joke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.joke_layout, parent, false) as TextView
        return JokeViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return jokeList.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.joke.text = jokeList[position].value     //correspond à la chaine de caractère (blague en elle même)
    }

    private fun List<String>.stringToJoke():List<Joke> = this.map { jokes_string_list ->
        Joke(
            categories = emptyList(),
            createdAt = "",
            updatedAt = "",
            url = "",
            id = "",
            iconUrl = "",
            value = jokes_string_list
        )}
}