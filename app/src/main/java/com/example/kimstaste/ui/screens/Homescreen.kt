package com.example.kimstaste.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kimstaste.navigation.Screen
import com.example.kimstaste.viewmodel.AuthViewModel

@Composable
fun Homescreen(navController: NavController,
               authViewModel: AuthViewModel = viewModel()){
    val goldColor = Color(0xFFD4AF37)

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center){

        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(text = "Welcome to KIMS TASTE", style = MaterialTheme.typography.headlineMedium, color = goldColor)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { navController.navigate(Screen.Booking.route) },
                colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Book a Suite", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.Profile.route) },
                colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("Go to Profile", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(Screen.MemberScreen.route) },
                colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text("join the fam ", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.logout()
                          navController.navigate(Screen.Login.route)
                          {popUpTo(Screen.Home.route){inclusive = true}}}

            ){
                Text("Logout")
            }
        }
    }
}
