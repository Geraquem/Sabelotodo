package com.mmfsin.sabelotodo.base

import android.app.Application
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        MobileAds.initialize(this) {}

//        getFCMToken()
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) Log.i("**** FCM **** ", it.result)
            else Log.i("FCM", "no token")
        }
    }
}