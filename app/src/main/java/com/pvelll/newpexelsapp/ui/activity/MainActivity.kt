package com.pvelll.newpexelsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //    private val navController = rememberNavController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition() {
            false
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}