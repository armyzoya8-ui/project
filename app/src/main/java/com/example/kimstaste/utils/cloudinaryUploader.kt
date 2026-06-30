package com.example.kimstaste.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * CloudinaryUploader provides a simplified, coroutine-powered way to upload images to Cloudinary.
 */
object CloudinaryUploader {

    /**
     * Uploads an image to Cloudinary using an unsigned upload preset.
     * Uses suspendCancellableCoroutine for proper lifecycle and cancellation support.
     * 
     * @param context The Android context.
     * @param imageUri The Uri of the image to be uploaded.
     * @param preset The Cloudinary unsigned upload preset name.
     * @param onProgress A callback for tracking upload progress (0.0 to 1.0).
     * @return The secure URL of the uploaded image.
     */
    suspend fun uploadImage(
        context: Context,
        imageUri: Uri,
        preset: String = "kimstaste_preset",
        onProgress: (Float) -> Unit = {}
    ): String = suspendCancellableCoroutine { continuation ->
        try {
            // Start the upload process and capture the requestId to allow for cancellation
            val requestId = MediaManager.get()
                .upload(imageUri)
                .option("resource_type", "auto")
                .unsigned(preset)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String?) {
                        onProgress(0f)
                    }

                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                        // totalBytes can be -1 if unknown, so we check for > 0
                        if (totalBytes > 0) {
                            onProgress(bytes.toFloat() / totalBytes.toFloat())
                        }
                    }

                    override fun onSuccess(requestId: String?, resultData: Map<Any?, Any?>?) {
                        // secure_url is preferred for HTTPS
                        val url = resultData?.get("secure_url") as? String 
                            ?: resultData?.get("url") as? String
                        
                        if (url != null) {
                            if (continuation.isActive) {
                                continuation.resume(url)
                            }
                        } else {
                            if (continuation.isActive) {
                                continuation.resumeWithException(Exception("Upload succeeded but URL is missing from result"))
                            }
                        }
                    }

                    override fun onError(requestId: String?, error: ErrorInfo?) {
                        val errorMessage = error?.description ?: "Unknown Cloudinary error"
                        if (continuation.isActive) {
                            continuation.resumeWithException(Exception(errorMessage))
                        }
                    }

                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                        val message = "Upload rescheduled: ${error?.description ?: "No details"}"
                        if (continuation.isActive) {
                            continuation.resumeWithException(Exception(message))
                        }
                    }
                })
                .dispatch(context)

            // Automatically cancel the Cloudinary request if the coroutine is cancelled
            continuation.invokeOnCancellation {
                if (requestId != null) {
                    MediaManager.get().cancelRequest(requestId)
                }
            }

        } catch (e: Exception) {
            if (continuation.isActive) {
                continuation.resumeWithException(e)
            }
        }
    }
}
