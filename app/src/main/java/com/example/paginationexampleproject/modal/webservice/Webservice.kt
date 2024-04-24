package com.example.paginationexampleproject.modal.webservice

import com.example.paginationexampleproject.modal.PlaceHolderModal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://jsonplaceholder.typicode.com/"
const val POSTS_API = "posts"

class WebService {
   private lateinit var apiInterface: APIInterface

   init {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
       apiInterface = retrofit.create(APIInterface::class.java)
   }

    suspend fun getPlaceHolderList() : MutableList<PlaceHolderModal> {
        return apiInterface.getPlaceHolderData()
    }

    internal interface APIInterface {
        @GET(BASE_URL + POSTS_API)
        suspend fun getPlaceHolderData(): MutableList<PlaceHolderModal>
    }
}