package com.example.kimstaste.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kimstaste.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // State for security gate and profile data
    var isUnlocked by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    // User details state
    var name by remember { mutableStateOf("Kims Guest") }
    var email by remember { mutableStateOf("guest@kimstaste.com") }
    var phone by remember { mutableStateOf("+254 700 000 000") }

    // Luxury Theme Colors
    val goldColor = Color(0xFFD4AF37)
    val darkBg = Color(0xFF1A1A1A)
    val cardBg = Color(0xFF2D2D2D)

    if (!isUnlocked) {
        // --- SECURE ACCESS GATEWAY ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(darkBg),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.VpnKey,
                    contentDescription = "Security",
                    tint = goldColor,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "GUEST SECURITY",
                    color = goldColor,
                    style = MaterialTheme.typography.headlineSmall,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Identification Required to view details",
                    color = Color.White.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { 
                        passwordInput = it
                        passwordError = false 
                    },
                    label = { Text("Suite Password", color = goldColor.copy(alpha = 0.6f)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    isError = passwordError,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = goldColor.copy(alpha = 0.2f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = goldColor,
                        errorBorderColor = Color.Red,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
                
                if (passwordError) {
                    Text(
                        text = "Incorrect password. Hint: Try 'admin'",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { 
                        // Simplified password check for demo purposes
                        if (passwordInput == "admin") {
                            isUnlocked = true 
                        } else {
                            passwordError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("ACCESS PROFILE", fontWeight = FontWeight.Bold, color = darkBg)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Return", color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
    } else {
        // --- AUTHENTICATED PROFILE VIEW ---
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { 
                        Text("GUEST PROFILE", color = goldColor, letterSpacing = 3.sp, fontWeight = FontWeight.ExtraBold) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { isUnlocked = false }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Lock", tint = goldColor)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = darkBg)
                )
            },
            containerColor = darkBg
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                
                // --- PROFILE PICTURE SECTION ---
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(cardBg)
                            .border(3.dp, goldColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = goldColor.copy(alpha = 0.2f),
                            modifier = Modifier.size(90.dp)
                        )
                    }
                    
                    Surface(
                        color = goldColor,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(42.dp)
                            .border(4.dp, darkBg, CircleShape)
                            .clickable { /* Trigger Image Picker logic */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Edit Picture",
                            tint = darkBg,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // --- GUEST DETAILS FORM ---
                ProfileDetailItem(
                    label = "Full Name",
                    value = name,
                    onValueChange = { name = it },
                    icon = Icons.Default.Badge,
                    goldColor = goldColor,
                    cardBg = cardBg
                )
                
                Spacer(modifier = Modifier.height(20.dp))

                ProfileDetailItem(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    icon = Icons.Default.Email,
                    goldColor = goldColor,
                    cardBg = cardBg
                )

                Spacer(modifier = Modifier.height(20.dp))

                ProfileDetailItem(
                    label = "Contact Number",
                    value = phone,
                    onValueChange = { phone = it },
                    icon = Icons.Default.Phone,
                    goldColor = goldColor,
                    cardBg = cardBg
                )

                Spacer(modifier = Modifier.weight(1f))

                Text("WELCOME TO KIMSFAM", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = darkBg)

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { 
                        // Pass the name to the member card screen using savedStateHandle
                        navController.currentBackStackEntry?.savedStateHandle?.set("userName", name)
                        navController.navigate(Screen.MemberCard.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                    shape = RoundedCornerShape(18.dp)
                ) {

                    Text("SAVE CHANGES", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = darkBg)
                }
            }
        }
    }
}

@Composable
fun ProfileDetailItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    goldColor: Color,
    cardBg: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = goldColor.copy(alpha = 0.6f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Icon(icon, contentDescription = null, tint = goldColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = goldColor,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                focusedContainerColor = cardBg,
                unfocusedContainerColor = cardBg,
                cursorColor = goldColor
            )
        )
    }
}
