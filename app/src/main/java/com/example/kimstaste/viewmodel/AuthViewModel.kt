package com.example.kimstaste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Updated UserProfile to include suitePassword and phone
data class UserProfile(
    val uid: String = "",
    val fullname: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "Guest",
    val suitePassword: String = "admin" // Default password
)

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class ProfileLoaded(val profile: UserProfile) : AuthState()
    data class Error(val message: String) : AuthState()
    object Logout : AuthState()
    object PasswordResetSent : AuthState()
}

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()

    init {
        checkUserStatus()
    }

    private fun checkUserStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _authState.value = AuthState.Success("Logged in as ${currentUser.email}")
            fetchUserProfile(currentUser.uid)
        } else {
            _authState.value = AuthState.Idle
        }
    }

    fun fetchUserProfile(uid: String) {
        viewModelScope.launch {
            try {
                val doc = firestore.collection("users").document(uid).get().await()
                if (doc.exists()) {
                    val profile = UserProfile(
                        uid = uid,
                        fullname = doc.getString("fullname") ?: "",
                        email = doc.getString("email") ?: "",
                        phone = doc.getString("phone") ?: "",
                        role = doc.getString("role") ?: "Guest",
                        suitePassword = doc.getString("suitePassword") ?: "admin"
                    )
                    _currentProfile.value = profile
                    _authState.value = AuthState.ProfileLoaded(profile)
                }
            } catch (e: Exception) {
                // Silent fail for status check
            }
        }
    }

    fun register(fullname: String, email: String, password: String, role: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                
                if (user != null) {
                    val userData = hashMapOf(
                        "uid" to user.uid,
                        "fullname" to fullname,
                        "email" to email,
                        "role" to role,
                        "suitePassword" to "admin", // Initial default
                        "createdAt" to System.currentTimeMillis()
                    )
                    
                    firestore.collection("users").document(user.uid).set(userData).await()
                    
                    _authState.value = AuthState.Success("Registration Successful")
                    fetchUserProfile(user.uid)
                } else {
                    _authState.value = AuthState.Error("User creation failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Registration Failed")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.Success("Login Successful")
                result.user?.let { fetchUserProfile(it.uid) }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login Failed")
            }
        }
    }

    // New function to update profile details including the suite password
    fun updateProfile(fullname: String, email: String, phone: String, suitePassword: String) {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val updates = mapOf(
                    "fullname" to fullname,
                    "email" to email,
                    "phone" to phone,
                    "suitePassword" to suitePassword
                )
                firestore.collection("users").document(uid).update(updates).await()
                fetchUserProfile(uid)
                _authState.value = AuthState.Success("Profile updated successfully")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Update failed")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.sendPasswordResetEmail(email).await()
                _authState.value = AuthState.PasswordResetSent
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to send reset email")
            }
        }
    }

    fun logout() {
        auth.signOut()
        _currentProfile.value = null
        _authState.value = AuthState.Logout
    }
}
