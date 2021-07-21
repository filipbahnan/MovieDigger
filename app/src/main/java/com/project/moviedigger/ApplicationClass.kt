package com.project.moviedigger

import android.app.Application
import com.onesignal.OneSignal

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
            // Logging set to help debug issues, remove before releasing your app.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

            // OneSignal Initialization
            OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        }
    }