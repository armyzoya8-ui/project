package com.example.kimstaste.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kimstaste.navigation.Screen
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kimstaste.viewmodel.AuthViewModel
import com.example.kimstaste.viewmodel.AuthState

@Composable
fun Splashscreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    // Collect the authentication state from the ViewModel
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(key1 = true) {
        // Run logo animations
        scale.animateTo(
            targetValue = 1f,
            animationSpec = interim(1000)
        )
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = interim(1000)
        )
        
        // Wait for a minimum time (e.g., 2 seconds) to ensure the splash is visible
        delay(2000L)

        // Navigate based on the current authentication state
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            is AuthState.Idle, is AuthState.Logout, is AuthState.Error, is AuthState.PasswordResetSent -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
                }
            is AuthState.Loading -> return@LaunchedEffect
            else -> {
                // Handle other states if needed
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A),
                        Color(0xFF2D2D2D)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Apartment,
                contentDescription = "Hotel Logo",
                tint = Color(0xFFD4AF37),
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale.value)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "KIMS TASTE",
                color = Color(0xFFD4AF37),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(alpha.value)
            )
            
            Text(
                text = "LUXURY HOTEL & RESORT",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(alpha.value)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            CircularProgressIndicator(
                color = Color(0xFFD4AF37),
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(30.dp)
                    .alpha(alpha.value)
            )
        }
    }
}

private fun <T> interim(duration: Int): AnimationSpec<T> = tween(
    durationMillis = duration,
    easing = FastOutSlowInEasing
)
