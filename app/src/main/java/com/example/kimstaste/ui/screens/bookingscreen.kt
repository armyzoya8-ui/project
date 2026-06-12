package com.example.kimstaste.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController) {
    val goldColor = Color(0xFFD4AF37)
    val darkBg = Color(0xFF1A1A1A)
    val cardBg = Color(0xFF2D2D2D)

    // State for booking details
    var selectedSuite by remember { mutableStateOf("Executive Suite") }
    var guests by remember { mutableStateOf("2 Guests") }
    var checkInDate by remember { mutableStateOf("24th May 2024") }
    var nights by remember { mutableIntStateOf(3) }
    var specialRequests by remember { mutableStateOf("") }
    
    val pricePerNight = when(selectedSuite) {
        "Presidential Suite" -> 1200
        "Executive Suite" -> 450
        "Deluxe Room" -> 250
        else -> 150
    }
    val totalPrice = pricePerNight * nights

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "RESERVATIONS",
                        color = goldColor,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = goldColor
                        )
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Tailor Your Luxury Stay",
                color = Color.White.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Suite Selection
            BookingSelectionItem(
                label = "Select Accommodation",
                value = selectedSuite,
                icon = Icons.Default.KingBed,
                goldColor = goldColor,
                cardBg = cardBg,
                onClick = { /* In a real app, show a BottomSheet or Dialog */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    BookingSelectionItem(
                        label = "Check-in",
                        value = checkInDate,
                        icon = Icons.Default.CalendarToday,
                        goldColor = goldColor,
                        cardBg = cardBg,
                        onClick = { }
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.weight(1f)) {
                    BookingSelectionItem(
                        label = "Nights",
                        value = "$nights Nights",
                        icon = Icons.Default.Nightlight,
                        goldColor = goldColor,
                        cardBg = cardBg,
                        onClick = { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            BookingSelectionItem(
                label = "Guests",
                value = guests,
                icon = Icons.Default.People,
                goldColor = goldColor,
                cardBg = cardBg,
                onClick = { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Special Requests
            Text(
                text = "SPECIAL REQUESTS",
                color = goldColor.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = specialRequests,
                onValueChange = { specialRequests = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Airport pickup, champagne on arrival, dietary needs...", color = Color.Gray.copy(alpha = 0.5f), fontSize = 14.sp) },
                shape = RoundedCornerShape(16.dp),
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

            Spacer(modifier = Modifier.height(40.dp))

            // Price Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, goldColor.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$selectedSuite x $nights", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text("$${pricePerNight * nights}", color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Luxury Tax & Service", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text("$0.00", color = Color.White, fontSize = 14.sp)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL PRICE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("$${totalPrice}.00", color = goldColor, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Handle actual booking logic/Firebase save */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                shape = RoundedCornerShape(18.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = darkBg)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "CONFIRM RESERVATION",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = darkBg
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BookingSelectionItem(
    label: String,
    value: String,
    icon: ImageVector,
    goldColor: Color,
    cardBg: Color,
    onClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            color = goldColor.copy(alpha = 0.6f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 6.dp)
        )
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            color = cardBg,
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = goldColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = goldColor.copy(alpha = 0.5f))
            }
        }
    }
}
