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
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration



class MainActivity : AppCompatActivity() {


    private var compositeDisposable = CompositeDisposable()
    private var jokeList: MutableList<Joke> = mutableListOf()
    private lateinit var adapter: JokeAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = JokeAdapter(jokeList)
        adapter.setOnShareCLickListener { id -> onShareClicked(id) }
        adapter.setOnSaveCLickListener { id, item -> onSaveClicked(id, item) }


        if (savedInstanceState == null)
            addJokeToList()
        else {
            savedInstanceState.getString("jokeList")?.let { jokeInJson ->
                val jokeDeserialized = Json(JsonConfiguration.Stable).parse(
                    Joke.serializer().list,
                    jokeInJson
                )
                jokeList.addAll(jokeDeserialized)
            }
        }

        val jokeTouchHelper = JokeTouchHelper(
            { position -> adapter.onJokeRemoved(position) },
            { from, to -> adapter.onItemMoved(from, to) })
        jokeTouchHelper.attachToRecyclerView(recycler_view)

        adapter.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                val progressBar: ProgressBar = findViewById<ProgressBar>(R.id.progressBar)
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



    override fun onSaveInstanceState(outState: Bundle) {
        val json = Json(JsonConfiguration.Stable)
        val jsonData = json.stringify(Joke.serializer().list, jokeList)
        outState.putString("jokeList",jsonData)
        super.onSaveInstanceState(outState)
    }



    override fun onDestroy() {

        super.onDestroy()
        compositeDisposable.clear()
    }




    private fun onSaveClicked(id: String, item:View){
        Log.wtf("joke_id", id )
        item as JokeView
        item.isSaved = !item.isSaved
    }

    private fun onShareClicked(id: String){
        Log.wtf("joke_id", id)
    }


}
