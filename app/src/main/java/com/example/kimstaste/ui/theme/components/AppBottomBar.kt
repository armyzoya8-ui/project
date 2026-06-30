package com.example.kimstaste.ui.theme.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomBar(
    currentRoute: String?,
    onDashboardClick: () -> Unit,
    onBookingClick: () -> Unit,
    onMembershipClick: () -> Unit,
    onUploadClick: () -> Unit,
    onProfileClick: () -> Unit
){
    val barBlue = Color(0xFF03A9F4)

    NavigationBar (
        containerColor = barBlue,
        modifier = Modifier.height(110.dp)
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.White,
            selectedTextColor = Color.White,
            unselectedIconColor = Color.White.copy(alpha = 0.6f),
            unselectedTextColor = Color.White.copy(alpha = 0.6f),
            indicatorColor = Color.White.copy(alpha = 0.2f)
        )
        
        NavigationBarItem(
            colors = itemColors,
            selected = currentRoute == "home_screen",
            onClick = onDashboardClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            colors = itemColors,
            selected = currentRoute == "booking_screen",
            onClick = onBookingClick,
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Booking") },
            label = { Text("Booking") }
        )
        NavigationBarItem(
            colors = itemColors,
            selected = currentRoute == "member_card",
            onClick = onMembershipClick,
            icon = { Icon(Icons.Default.CardMembership, contentDescription = "Membership") },
            label = { Text("Member") }
        )
        NavigationBarItem(
            colors = itemColors,
            selected = currentRoute == "upload_media",
            onClick = onUploadClick,
            icon = { Icon(Icons.Default.Upload, contentDescription = "Upload") },
            label = { Text("Upload") }
        )
        NavigationBarItem(
            colors = itemColors,
            selected = currentRoute == "profile_screen",
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") }
        )
    }
}
