package com.example.kimstaste.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kimstaste.viewmodel.MediaState
import com.example.kimstaste.viewmodel.MediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMediaScreen(
    navController: NavController,
    mediaId: String,
    mediaViewModel: MediaViewModel = viewModel()
) {
    val publicMedia by mediaViewModel.publicMedia.collectAsState()
    val myMedia by mediaViewModel.myMedia.collectAsState()
    
    // Find the item in either list
    val mediaItem = (publicMedia + myMedia).find { it.id == mediaId }

    var title by remember { mutableStateOf(mediaItem?.title ?: "") }
    var description by remember { mutableStateOf(mediaItem?.description ?: "") }
    var isPublic by remember { mutableStateOf(mediaItem?.isPublic ?: true) }

    val mediaState by mediaViewModel.mediaState.collectAsState()
    val isLoading = mediaState is MediaState.Loading
    val errorMessage = (mediaState as? MediaState.Error)?.message

    LaunchedEffect(Unit) {
        mediaViewModel.loadMyMedia()
        mediaViewModel.loadPublicMedia()
    }

    LaunchedEffect(mediaState) {
        if (mediaState is MediaState.ActionSuccess) {
            mediaViewModel.clearState()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Media") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Public Visibility", modifier = Modifier.weight(1f))
                Switch(
                    checked = isPublic,
                    onCheckedChange = { isPublic = it },
                    enabled = !isLoading
                )
            }

            Button(
                onClick = {
                    mediaViewModel.updateMedia(mediaId, title, description, isPublic)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && title.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Update Media")
                }
            }
            
            OutlinedButton(
                onClick = {
                    mediaItem?.let { mediaViewModel.deleteMedia(it) }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                enabled = !isLoading
            ) {
                Text("Delete Media")
            }
        }
    }
}
