package com.example.kimstaste.MODEL

data class MediaItem(
    val id: String = "",
    val fullname: String = "",
    val email: String = "",
    val GuestName: String = "",
    val ownerId: String = "",


    // current time

){
    // firestore needs what we call a non-arg constructor
    // something to map the values to the firestore collection
    fun toMap(): Map<String,Any> = mapOf(
        "id" to id,
        "fullname" to fullname,
        "email" to email,
        "GuestName" to GuestName,
        "ownerId" to ownerId,
    )
}
