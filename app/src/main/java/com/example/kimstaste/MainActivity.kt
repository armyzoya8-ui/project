package com.example.kimstaste

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.kimstaste.navigation.KimstasteNavGraph
import com.example.kimstaste.ui.theme.KIMSTASTETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Tells the OS to let Compose handle full bleed layouts seamlessly
        enableEdgeToEdge()

        setContent {
            KIMSTASTETheme {
                // Initialize the central navigation state tracker
                val navController = rememberNavController()

                // Inject the controller into your central routing desk
                KimstasteNavGraph(navController = navController)
            }
        }
    }
}
