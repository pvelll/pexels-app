package com.pvelll.newpexelsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.databinding.ActivityMainBinding
import com.pvelll.newpexelsapp.domain.connectivity.ConnectivityObserver
import com.pvelll.newpexelsapp.domain.usecases.LoadStatus
import com.pvelll.newpexelsapp.ui.viewmodels.HomeViewModel
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val homeViewModel  = ViewModelProvider(this)[HomeViewModel::class.java]
        val networkConnectivityObserver by inject<NetworkConnectivityObserver>(NetworkConnectivityObserver::class.java)
        installSplashScreen().setKeepOnScreenCondition {
            if (networkConnectivityObserver.isConnected()) {
                homeViewModel.curatedPhotosLoadStatus.value != LoadStatus.SUCCESS && homeViewModel.curatedPhotosLoadStatus.value != LoadStatus.NO_INTERNET
            } else {
                false
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}



