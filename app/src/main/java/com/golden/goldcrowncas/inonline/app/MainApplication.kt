package com.golden.goldcrowncas.inonline.app

import android.app.Application
import com.onesignal.OneSignal
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        OneSignal.initWithContext(this, "5065ef78-4db7-41d6-8762-b88ff66b84da")
        CoroutineScope(Dispatchers.IO).launch {
            OneSignal.Notifications.requestPermission(true)
        }
    }
}