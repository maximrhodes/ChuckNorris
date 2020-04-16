package com.example.chucknorris


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.joke_layout.view.*
import java.util.*

class JokeAdapter  (private val jokeList: List<Joke>): RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    private lateinit var myOnBottomReachedListener: OnBottomReachedListener
    private var myOnShareClickListener: (value: String) -> Unit = {}
    private var myOnSaveClickListener: (id: Joke, saved: Boolean) -> Unit = {_, _ ->}


    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.myOnBottomReachedListener = onBottomReachedListener
    }

    fun setOnShareClickListener(onShareClickListener: (value: String) -> Unit) {
        myOnShareClickListener = onShareClickListener
    }

    fun setOnSaveClickListener(onSaveClickListener: (id: Joke, saved: Boolean) -> Unit = {_, _ ->}) {
        myOnSaveClickListener = onSaveClickListener
    }


    class JokeViewHolder(val joke: JokeView) : RecyclerView.ViewHolder(joke)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val jokeView = JokeView(parent.context)
        return JokeViewHolder(jokeView)
    }


    override fun getItemCount(): Int {
        return jokeList.size
    }




    fun onItemMoved(from: Int, to: Int) {
        if (from < to)
            (from until to).forEach {
                Collections.swap(jokeList, it, it + 1)
            }
        else
            (to until from).forEach {
                Collections.swap(jokeList, it, it + 1)
            }
        this.notifyItemMoved(from, to)
    }

    fun onJokeRemoved(position: Int) {
        jokeList.toMutableList().removeAt(position)
        this.notifyItemRemoved(position)
    }





    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.joke.setUpView(JokeView.Model(jokeList[position],false, myOnShareClickListener, myOnSaveClickListener))
        holder.joke.imageShare.setOnClickListener { myOnShareClickListener(jokeList[position].id) }
        holder.joke.imageStar.setOnClickListener {
            myOnSaveClickListener(jokeList[position],false)
            if (position == jokeList.size - 1) {
                myOnBottomReachedListener.onBottomReached(position)
            }
        }
    }
}