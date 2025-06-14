package com.example.clima

import android.app.Application
import com.google.firebase.FirebaseApp

class ShopifyKaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
