package com.example.chucknorris

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JokeAdapter  (private val jokeList: List<Joke>): RecyclerView.Adapter<JokeAdapter.JokeViewHolder>(){

    lateinit var myOnBottomReachedListener: OnBottomReachedListener

    class JokeViewHolder(val joke: TextView) : RecyclerView.ViewHolder(joke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.joke_layout, parent, false) as TextView
        return JokeViewHolder(textView)
    }

    override fun getItemCount(): Int {
        return jokeList.size
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        if (position == jokeList.size - 1){

            myOnBottomReachedListener.onBottomReached(position)

        }
        holder.joke.text = jokeList[position].value     //correspond à la chaine de caractère (blague en elle même)
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.myOnBottomReachedListener = onBottomReachedListener
    }
}