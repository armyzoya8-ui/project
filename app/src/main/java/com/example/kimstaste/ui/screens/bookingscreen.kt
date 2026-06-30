package com.example.kimstaste.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kimstaste.navigation.Screen
import com.example.kimstaste.ui.theme.components.AppBottomBar
import com.example.kimstaste.viewmodel.MediaState
import com.example.kimstaste.viewmodel.MediaViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.platform.LocalUriHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(navController: NavController,
                  mediaViewModel: MediaViewModel = viewModel()
){
    val goldColor = Color(0xFFD4AF37)
    val darkBg = Color(0xFF1A1A1A)
    val cardBg = Color(0xFF2D2D2D)
    val uriHandler = LocalUriHandler.current

    // State for booking details
    var selectedSuite by remember { mutableStateOf("Deluxe Room") }
    var guests by remember { mutableStateOf("1") }
    var checkInDate by remember { mutableStateOf("") }
    var nights by remember { mutableStateOf("1") }
    var specialRequests by remember { mutableStateOf("") }
    
    // UI selection state
    var showDatePicker by remember { mutableStateOf(false) }
    
    val suites = listOf(
        SuiteInfo("Presidential Suite", 200000, Icons.Default.KingBed),
        SuiteInfo("Executive Suite", 150000, Icons.Default.MeetingRoom),
        SuiteInfo("Deluxe Room", 100000, Icons.Default.Bed),
        SuiteInfo("Standard Room", 70000, Icons.Default.SingleBed)
    )

    val datePickerState = rememberDatePickerState()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    // Update date string when selected
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            checkInDate = formatter.format(Date(it))
        }
    }

    // Handle successful booking
    LaunchedEffect(mediaState) {
        if (mediaState is MediaState.ActionSuccess) {
            mediaViewModel.clearState()
            navController.popBackStack()
        }
    }

    val n = nights.toIntOrNull() ?: 1
    val currentSuiteInfo = suites.find { it.name == selectedSuite } ?: suites[2]
    val pricePerNight = currentSuiteInfo.price
    val totalPrice = pricePerNight * n
    val isLoading = mediaState is MediaState.Loading

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
        containerColor = darkBg,
        bottomBar = {
            AppBottomBar(
                currentRoute = "booking_screen",
                onDashboardClick = { navController.navigate(Screen.Home.route) },
                onBookingClick = { /* Already here */ },
                onMembershipClick = { navController.navigate(Screen.MemberCard.route) },
                onUploadClick = { navController.navigate(Screen.UploadMedia.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }
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

            // Suite Selection - Visible Cards
            Text(
                text = "SELECT ACCOMMODATION",
                color = goldColor.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 12.dp)
            )
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(suites) { suite ->
                    SuiteCard(
                        suite = suite,
                        isSelected = selectedSuite == suite.name,
                        onSelect = { selectedSuite = suite.name },
                        goldColor = goldColor,
                        cardBg = cardBg
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    BookingSelectionItem(
                        label = "Check-in",
                        value = if (checkInDate.isEmpty()) "Select Date" else checkInDate,
                        icon = Icons.Default.CalendarToday,
                        goldColor = goldColor,
                        cardBg = cardBg,
                        onClick = { showDatePicker = true }
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                
                // Nights Input with Numeric Keyboard
                OutlinedTextField(
                    value = nights,
                    onValueChange = { if (it.all { char -> char.isDigit() }) nights = it },
                    label = { Text("Nights", color = goldColor.copy(alpha = 0.6f), fontSize = 11.sp) },
                    modifier = Modifier.weight(1f),
                    leadingIcon = { Icon(Icons.Default.Nightlight, contentDescription = null, tint = goldColor, modifier = Modifier.size(18.dp)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = goldColor,
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Guests Input with Numeric Keyboard
            OutlinedTextField(
                value = guests,
                onValueChange = { if (it.all { char -> char.isDigit() }) guests = it },
                label = { Text("Number of Guests", color = goldColor.copy(alpha = 0.6f), fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = goldColor, modifier = Modifier.size(18.dp)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                enabled = !isLoading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = goldColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = goldColor,
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg
                )
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
                    .height(100.dp),
                placeholder = { Text("Airport pickup, dietary needs...", color = Color.Gray.copy(alpha = 0.5f), fontSize = 14.sp) },
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                enabled = !isLoading,
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

            Spacer(modifier = Modifier.height(32.dp))

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
                        Text("$selectedSuite x $n", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text("Ksh ${pricePerNight * n}", color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Luxury Tax & Service", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text("Ksh 0", color = Color.White, fontSize = 14.sp)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.White.copy(alpha = 0.1f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL PRICE", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text("Ksh $totalPrice", color = goldColor, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    uriHandler.openUri(
                        "https://gyration-universe-confider.ngrok-free.dev/mediampesa/"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Pay with M-Pesa", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mediaState is MediaState.Error) {
                Text(
                    text = (mediaState as MediaState.Error).message,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    val suiteInfo = suites.find { it.name == selectedSuite }
                    if (suiteInfo != null) {
                        mediaViewModel.bookings(
                            suiteInfo = suiteInfo,
                            guests = guests,
                            checkInDate = checkInDate,
                            nights = nights,
                            specialRequests = specialRequests
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = goldColor),
                shape = RoundedCornerShape(18.dp),
                enabled = !isLoading && checkInDate.isNotBlank(),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = darkBg, modifier = Modifier.size(24.dp))
                } else {
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
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK", color = goldColor)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = cardBg)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = goldColor,
                    headlineContentColor = goldColor,
                    selectedDayContainerColor = goldColor,
                    selectedDayContentColor = darkBg,
                    todayContentColor = goldColor,
                    todayDateBorderColor = goldColor
                )
            )
        }
    }
}

data class SuiteInfo(val name: String, val price: Int, val icon: ImageVector)

@Composable
fun SuiteCard(
    suite: SuiteInfo,
    isSelected: Boolean,
    onSelect: () -> Unit,
    goldColor: Color,
    cardBg: Color
) {
    Surface(
        modifier = Modifier
            .width(140.dp)
            .height(110.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) goldColor else cardBg,
        border = if (isSelected) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                suite.icon,
                contentDescription = null,
                tint = if (isSelected) Color(0xFF1A1A1A) else goldColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = suite.name,
                color = if (isSelected) Color(0xFF1A1A1A) else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ksh ${suite.price}",
                color = if (isSelected) Color(0xFF1A1A1A).copy(alpha = 0.7f) else goldColor.copy(alpha = 0.8f),
                fontSize = 11.sp
            )
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
