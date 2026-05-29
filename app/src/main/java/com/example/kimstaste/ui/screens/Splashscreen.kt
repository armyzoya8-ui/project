package com.example.kimstaste.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
//to maintain state i.e data inside this screen
import androidx.compose.runtime.remember
import androidx.navigation.NavController
// animation effects
//import androidx.compose.animation.core
// allows us to select a background effect for this screen
// layout configuration
// material design imports
// all compose runtime features
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


// Ui alignments , drawing and scaling(i.e. measurements)
// navigation
// screens
import com.example.kimstaste.navigation.Screen
import com.example.kimstaste.ui.theme.KIMSTASTETheme
//timer delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun Splashscreen(navController: NavController){
    // animating my logo scale
    val scale = remember { Animatable(initialValue = 0f) }
    //launched effect to delay splash screen showcase
    // basically we want the screen to be in view for a number
    //of seconds
    LaunchedEffect(key1 = Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow

            )
        )
        delay(timeMillis = 1500) //splash screen to last 1.5 seconds
        // after the 1.5 seconds we then redirect user to the
        // login screen
        navController.navigate(Screen.Login.route){
            //splash will become the backstack screen
            //i.e when user presses back from login
            popUpTo(Screen.Splash.route){
                inclusive = true
            }
        }


    }
    //define our logo
    // Box : another example of a container for hosting composable elements
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center

    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally)
        {
            //ICON and Text together
            Icon(
                imageVector = Icons.Default.VideoLibrary,
                contentDescription =  "Kimstaste Logo" ,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(96.dp)
                    .scale(scale.value)



            )
            Spacer(Modifier.height(16.dp))
            Text(text = "Kimstaste",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = 0.6f))
            Text(text = "Your classroom media library",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
                    .copy(alpha = 0.6f))
        }



    }

}
@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    KIMSTASTETheme() {
        SplashScreen(navController = rememberNavController())
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    TODO("Not yet implemented")
}
