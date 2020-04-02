package com.example.chucknorris

import io.reactivex.Observable
import retrofit2.http.GET

interface JokeApiService {
    @GET("jokes/random")
    fun giveMeAJoke() : Observable<Joke>
}