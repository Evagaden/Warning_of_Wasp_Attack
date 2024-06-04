package com.example.waspas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.compose.WarningOfWaspAttackTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val notificationMessage = intent.getStringExtra("notificationInfo")?:""
        Log.d("MainActivityM", notificationMessage)
        val timestamp = intent.getStringExtra("timestamp")?:""
        Log.d("MainActivityT", timestamp)
        super.onCreate(savedInstanceState)
        setContent {
            WarningOfWaspAttackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    App(notification = notificationMessage, timestamp)
                }
            }
        }
    }

}


