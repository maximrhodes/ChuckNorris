package com.example.chucknorris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE

import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {


    private var compositeDisposable = CompositeDisposable()
    private var jokeList = mutableListOf<Joke>()
    private lateinit var adapter: JokeAdapter
    private val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = JokeAdapter(jokeList)
        addJokeToList()

        adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                progressBar.visibility = VISIBLE
                addJokeToList()
                progressBar.visibility = INVISIBLE
            }
        })
    }


    private fun addJokeToList(){
        val joke = JokeApiServiceFactory.getJokeApiService().giveMeAJoke();

        val selectedJoke = joke.repeat(10).subscribeOn( Schedulers.io() ).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
            onError = { error : Throwable -> Log.e( "Error", "Erreur dans le chargement : $error" ) },
            onNext = { selectedJoke -> Log.i( "joke", "$selectedJoke")
                jokeList.add(selectedJoke)
                adapter.notifyDataSetChanged()},
            onComplete = {}
        )

        compositeDisposable.add(selectedJoke)
    }



    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }



}
