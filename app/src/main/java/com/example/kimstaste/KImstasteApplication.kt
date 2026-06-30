package com.example.kimstaste



import android.app.Application
import com.cloudinary.android.MediaManager


class KimstasteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //config cloudinary by pointing to the cloudinary folder
        val config = mapOf(
            "cloud_name" to "dywhzl5pc" // get cloud name from cloudinary
        )
        MediaManager.init(this, config)
    }
}
