package com.doodle.turboracing3.di.startup

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class FirebaseAppInitializer : Initializer<FirebaseApp?> {
    override fun create(context: Context): FirebaseApp {
        val options = FirebaseOptions.fromResource(context)
            ?: throw IllegalArgumentException("FirebaseOptions cannot be null")
        return FirebaseApp.initializeApp(context, options)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
