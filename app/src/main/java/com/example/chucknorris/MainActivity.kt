package com.example.chucknorris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("List", Jokes.jokes_string_list.toString())

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = JokeAdapter()

        val joke = JokeApiServiceFactory.getJokeApiService().giveMeAJoke();

        val selectedJoke = joke.subscribeOn( Schedulers.io() ).subscribeBy(
                onError = { error : Throwable -> Log.e( "Error", "Erreur dans le chargement : $error" ) },
                onSuccess = { selectedJoke -> Log.i( "joke", "$selectedJoke")}
            )
    }
}
