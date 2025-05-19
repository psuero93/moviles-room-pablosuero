package com.pablosuero.apppablo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.pablosuero.apppablo.ui.navigation.AppNavigation
import com.pablosuero.apppablo.ui.theme.AppPabloTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPabloTheme {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        }
    }
}