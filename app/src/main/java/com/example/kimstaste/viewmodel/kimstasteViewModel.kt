package com.example.kimstaste.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kimstaste.MODEL.MediaItem
import com.example.kimstaste.ui.screens.SuiteInfo
import com.example.kimstaste.utils.CloudinaryUploader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class MediaState {
    object Idle : MediaState()
    object Loading : MediaState()
    object ActionSuccess : MediaState()
    data class Success(val items: List<MediaItem>) : MediaState()
    data class Error(val message: String) : MediaState()
}

class MediaViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _publicMedia = MutableStateFlow<List<MediaItem>>(emptyList())
    val publicMedia: StateFlow<List<MediaItem>> = _publicMedia

    private val _myMedia = MutableStateFlow<List<MediaItem>>(emptyList())
    val myMedia: StateFlow<List<MediaItem>> = _myMedia

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Idle)
    val mediaState: StateFlow<MediaState> = _mediaState

    private val _uploadProgress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _uploadProgress

    fun loadPublicMedia() {
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                val snapshot = db.collection("media")
                    .whereEqualTo("isPublic", true)
                    .orderBy("uploadedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()
                
                val items = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MediaItem::class.java)?.copy(id = doc.id)
                }
                _publicMedia.value = items
                _mediaState.value = MediaState.Success(items)
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Failed to load public media")
            }
        }
    }

    fun loadMyMedia() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                val snapshot = db.collection("media")
                    .whereEqualTo("ownerId", uid)
                    .orderBy("uploadedAt", Query.Direction.DESCENDING)
                    .get()
                    .await()
                
                val items = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MediaItem::class.java)?.copy(id = doc.id)
                }
                _myMedia.value = items
                _mediaState.value = MediaState.Success(items)
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Failed to load your media")
            }
        }
    }

    fun uploadMedia(
        context: Context,
        title: String,
        description: String,
        category: String,
        isPublic: Boolean,
        mediaUri: Uri,
        ownerName: String
    ) {
        val uid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                val url = CloudinaryUploader.uploadImage(
                    context,
                    mediaUri,
                    onProgress = { _uploadProgress.value = it }
                )

                val mediaItem = MediaItem(
                    title = title,
                    description = description,
                    ImageUrl = url,
                    ownerId = uid,
                    ownerName = ownerName,
                    category = category,
                    isPublic = isPublic,
                    uploadedAt = System.currentTimeMillis()
                )

                db.collection("media").add(mediaItem.toMap()).await()
                _uploadProgress.value = 0f
                _mediaState.value = MediaState.ActionSuccess

                loadPublicMedia()
                loadMyMedia()
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Something went wrong during upload")
            }
        }
    }

    fun updateMedia(
        mediaId: String,
        title: String,
        description: String,
        isPublic: Boolean
    ) {
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                db.collection("media").document(mediaId).update(
                    mapOf(
                        "title" to title,
                        "description" to description,
                        "isPublic" to isPublic
                    )
                ).await()
                _mediaState.value = MediaState.ActionSuccess
                loadMyMedia()
                loadPublicMedia()
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Something went wrong during update")
            }
        }
    }

    fun deleteMedia(item: MediaItem) {
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                db.collection("media").document(item.id).delete().await()
                _mediaState.value = MediaState.ActionSuccess
                loadMyMedia()
                loadPublicMedia()
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Something went wrong during delete")
            }
        }
    }
    
    fun bookings(
        suiteInfo: SuiteInfo,
        guests: String,
        checkInDate: String,
        nights: String,
        specialRequests: String
    ) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _mediaState.value = MediaState.Loading
            try {
                db.collection("bookings").add(
                    mapOf(
                        "userId" to uid,
                        "suiteName" to suiteInfo.name,
                        "suitePrice" to suiteInfo.price,
                        "guests" to guests,
                        "checkInDate" to checkInDate,
                        "nights" to nights,
                        "specialRequests" to specialRequests,
                        "timestamp" to System.currentTimeMillis()
                    )
                ).await()
                _mediaState.value = MediaState.ActionSuccess
            } catch (e: Exception) {
                _mediaState.value = MediaState.Error(e.message ?: "Booking failed. Please try again.")
            }
        }
    }

    fun clearState() {
        _mediaState.value = MediaState.Idle
    }
}
