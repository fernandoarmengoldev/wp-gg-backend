package com.fernandoarmengol.ggwp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //Obtencion de datos de NodeJS en LocalHost
    fun getRetrofitNodeJSLocalHost(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //Obtencion de datos de NodeJS en Internet
    fun getRetrofitNodeJS(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://lol-app-server.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //Obtencion de Datos del Comunity Dragon
    fun getRetrofitComunityDragon(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}