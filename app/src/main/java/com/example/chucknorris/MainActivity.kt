package com.example.chucknorris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import io.reactivex.android.schedulers.AndroidSchedulers


class MainActivity : AppCompatActivity() {


    var compositeDisposable = CompositeDisposable();
    var jokeList = mutableListOf<Joke>();
    lateinit var adapter: RecyclerView.Adapter<*>;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        adapter = JokeAdapter(jokeList)
        addJokeToList()
    }


    fun addJokeToList(){
        val joke = JokeApiServiceFactory.getJokeApiService().giveMeAJoke();

        val selectedJoke = joke.subscribeOn( Schedulers.io() ).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
            onError = { error : Throwable -> Log.e( "Error", "Erreur dans le chargement : $error" ) },
            onSuccess = { selectedJoke -> Log.i( "joke", "$selectedJoke")
                jokeList.add(selectedJoke)}
        )

        adapter.notifyDataSetChanged();

        compositeDisposable.add(selectedJoke);
    }


    fun jokeOnClick(view: View){
        addJokeToList()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }



}
