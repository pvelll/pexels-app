package com.pvelll.newpexelsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.databinding.ActivityMainBinding
import com.pvelll.newpexelsapp.domain.usecases.LoadStatus
import com.pvelll.newpexelsapp.ui.viewmodels.HomeViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel  = ViewModelProvider(this)[HomeViewModel::class.java]
        installSplashScreen().setKeepOnScreenCondition() {
            homeViewModel.curatedPhotosLoadStatus.value != LoadStatus.SUCCESS && homeViewModel.curatedPhotosLoadStatus.value != LoadStatus.NO_INTERNET
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

