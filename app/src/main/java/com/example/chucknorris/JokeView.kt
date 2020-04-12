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


    var isSaved: Boolean = false
        set(value) {
            when (value) {
                true ->  changeStar(R.drawable.ic_star_black_24dp)
                false -> changeStar(R.drawable.ic_star_border_black_24dp)
            }
            field = value
        }



    init {
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(R.layout.joke_layout, this)
        this.layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    }


    data class Model(val value: String)

    fun setUpView(model: Model) {
        jokeText.text = model.value
    }


    private fun changeStar(im: Int) {
        imageStar.setImageResource(im)
    }


}

