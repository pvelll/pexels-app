package com.pvelll.newpexelsapp.di

import com.pvelll.newpexelsapp.BuildConfig
import com.pvelll.newpexelsapp.data.api.PexelApi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "8ALtmQPhjTjdkUVCEMTtR6uTJTw9NLB2mEE0XwqSS6RFlTsV3moGwQwX")
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