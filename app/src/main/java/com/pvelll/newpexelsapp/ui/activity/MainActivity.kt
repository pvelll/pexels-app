package com.pvelll.newpexelsapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.compose.rememberNavController
import com.pvelll.newpexelsapp.R

class MainActivity : AppCompatActivity() {
//    private val navController = rememberNavController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}