package com.example.chucknorris


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.joke_layout.view.*

class JokeAdapter  (private val jokeList: List<Joke>): RecyclerView.Adapter<JokeAdapter.JokeViewHolder>() {

    private lateinit var myOnBottomReachedListener: OnBottomReachedListener
    private var myOnShareClickListener: (id: String) -> Unit = {}
    private var myOnSaveClickListener: (id: String, item: View) -> Unit = { _: String, _: View -> }


    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {

        this.myOnBottomReachedListener = onBottomReachedListener
    }

    fun setOnShareCLickListener(onShareClickListener: (id: String) -> Unit) {
        myOnShareClickListener = onShareClickListener
    }

    fun setOnSaveCLickListener(onSaveClickListener: (id: String, item: View) -> Unit) {
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


    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        holder.joke.setUpView(JokeView.Model(jokeList[position].value))
        holder.joke.imageShare.setOnClickListener { myOnShareClickListener(jokeList[position].id) }
        holder.joke.imageStar.setOnClickListener {
            myOnSaveClickListener(jokeList[position].id, holder.joke)
            if (position == jokeList.size - 1) {
                myOnBottomReachedListener.onBottomReached(position)
            }
        }
    }
}