package com.pvelll.newpexelsapp

import android.app.Application
import com.pvelll.newpexelsapp.di.apiModule
import com.pvelll.newpexelsapp.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class App : Application() {
    override fun onCreate(){
        super.onCreate()
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(apiModule, databaseModule)
        }
    }
}