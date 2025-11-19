package com.example.hunrmand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.hunrmand.ui.screens.MainScreen
import com.example.hunrmand.ui.theme.HunrmandTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HunrmandTheme {
                MainScreen()
            }
        }
    }
}