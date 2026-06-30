package com.example.kimstaste.MODEL

data class Profile(
    val uid: String = "",
    val fullname: String = "",
    val email: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "fullName" to fullname,
        "email" to email
    )
}

data class MediaItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val ImageUrl: String = "",
    val ownerId: String = "",
    val ownerName: String = "",
    val category: String = "",
    val isPublic: Boolean = false,
    val uploadedAt: Long = System.currentTimeMillis(),
    val fullname: String = "",
    val email: String = "",
    val GuestName: String = ""
) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "description" to description,
        "ImageUrl" to ImageUrl,
        "ownerId" to ownerId,
        "ownerName" to ownerName,
        "category" to category,
        "isPublic" to isPublic,
        "uploadedAt" to uploadedAt,
        "fullname" to fullname,
        "email" to email,
        "GuestName" to GuestName
    )
}
