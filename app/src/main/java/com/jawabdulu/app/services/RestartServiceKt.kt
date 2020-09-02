package com.jawabdulu.app.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

class RestartServiceKt : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "onReceive: service tried to stop")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(Intent(context, BackgroundServiceKt::class.java))
            } else {
                context.startService(Intent(context, BackgroundServiceKt::class.java))
            }
        }
        Log.d(TAG, "onReceive: service tried to stop")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, BackgroundServiceKt::class.java))
        } else {
            context.startService(Intent(context, BackgroundServiceKt::class.java))
        }
    }

    companion object {
        private const val TAG = "RestartService"
    }
}