package com.mimikunda.gripgraf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mimikunda.gripgraf.ui.navigation.MainNavigation
import com.mimikunda.gripgraf.ui.theme.GripGrafTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GripGrafTheme {
                //Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                MainNavigation(
                  //  modifier = Modifier.padding(innerPadding)
                )
                //}
            }
        }
    }
}

