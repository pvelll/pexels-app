package com.pvelll.newpexelsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.databinding.ActivityMainBinding
import com.pvelll.newpexelsapp.di.appModule
import kotlinx.coroutines.delay
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition() {
            false
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}
