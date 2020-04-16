package com.example.chucknorris

import android.content.Context
import android.content.Intent
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
    private val jokeSavedList: MutableList<Joke> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        val sharedPreferences = getSharedPreferences("jokeShared", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("jokeSaved")) {
            Log.wtf("jokepref", sharedPreferences.getString("jokeSaved", ""))
            jokeSavedList.addAll(
                sharedPreferences.getString("jokeSaved", "")?.let {
                    Json(JsonConfiguration.Stable).parse(
                        Joke.serializer().list, it
                    )
                }!!
            )
            jokeList.addAll(jokeSavedList)

        }




        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = JokeAdapter(jokeList)
        adapter.setOnShareClickListener { value -> onShareClicked(value) }
        adapter.setOnSaveClickListener { joke: Joke, isSaved: Boolean -> onSaveClicked(joke, isSaved) }


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




    private fun onSaveClicked(joke: Joke, isSaved: Boolean) {
        if (isSaved)
            jokeSavedList.add(joke)
        else
            jokeSavedList.remove(joke)
        val sharedPreferences = getSharedPreferences("jokeShared", Context.MODE_PRIVATE)
        val json = Json(JsonConfiguration.Stable).stringify(Joke.serializer().list, jokeSavedList)

        Log.wtf("Save", "$isSaved: $json")
        sharedPreferences.edit()
            .putString("jokeSaved", json)
            .apply()
    }

    private fun onShareClicked(id: String){
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, id)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


}
