package com.example.kimstaste.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.kimstaste.viewmodel.UserProfile
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color


@Composable
fun AppDrawer(
    profile: UserProfile?,
    currentRoute: String?,
    onDashboardClick: () -> Unit,
    onUploadClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    //role reference

    //container for the drawer
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    profile?.fullname?.firstOrNull()?. uppercase() ?:"user",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary

                )


            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                profile?.fullname ?: "User",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

        }
        Spacer(modifier = Modifier.height(16.dp))
        NavigationDrawerItem(
            label = { Text("Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = onDashboardClick,
            icon = { Icon(Icons.Default.Dashboard,null)},
            modifier = Modifier.padding(horizontal = 12.dp)

        )
        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = currentRoute == "Profile",
            onClick = onUploadClick,
            icon = { Icon(Icons.Default.Upload,null)},
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))



        Spacer(modifier = Modifier.height(16.dp))
        Divider(modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))


        NavigationDrawerItem(
            label = { Text("Logout") },
            selected = false,
            onClick = onLogoutClick,
            icon = { Icon(Icons.Default.Logout,null)},
            modifier = Modifier.padding(vertical= 12.dp),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.surface,
            )


        )

    }
}
