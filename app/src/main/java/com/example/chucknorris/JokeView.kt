package com.example.chucknorris

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import kotlinx.android.synthetic.main.joke_layout.view.*


class JokeView @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyleAttr: Int = 0) : ConstraintLayout( context, attrs, defStyleAttr) {




    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.joke_layout, this)
        this.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }


    data class Model(val joke: Joke, val isSaved: Boolean, val myOnShareClickListener: (id: String) -> Unit = {}, val myOnSaveClickListener: (joke: Joke, saved: Boolean) -> Unit = { _, _ -> })

    fun setUpView(model: Model) {
        jokeText.text = model.joke.value
        changeStar(model.isSaved)
        imageShare.setOnClickListener { model.myOnShareClickListener(model.joke.value) }
        imageStar.setOnClickListener {
            model.myOnSaveClickListener(model.joke,!model.isSaved)
            changeStar(model.isSaved)
            setUpView(model.copy(isSaved=!model.isSaved))
        }
    }



    private fun changeStar(isSaved: Boolean) {
        if (isSaved){
            imageStar.setImageResource(R.drawable.ic_star_black_24dp)
        }
        else {
            imageStar.setImageResource(R.drawable.ic_star_border_black_24dp)
        }
    }


}

