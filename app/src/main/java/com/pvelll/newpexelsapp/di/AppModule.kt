package com.pvelll.newpexelsapp.di

import androidx.room.Room
import com.pvelll.newpexelsapp.BuildConfig
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    single{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", BuildConfig.API_KEY)
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()
        Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PexelApi::class.java)

    }
}

val databaseModule = module{
    single{
        Room.databaseBuilder(get(), PhotoDatabase::class.java, "photos").build()
    }
    single {
        get<PhotoDatabase>().photoDao()
    }
}