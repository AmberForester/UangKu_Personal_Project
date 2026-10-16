package com.example.uangku.feature.backup.data.automatic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val preferences = AutoBackupPreferences(context)
                    val settings = preferences.settings.firstOrNull()

                    if (settings != null && settings.enabled) {
                        Log.d("BootReceiver", "Device rebooted, rescheduling auto backup with frequency ${settings.frequency}")
                        AutoBackupScheduler(context).scheduleNext(settings.frequency)
                    }
                } catch (e: Exception) {
                    Log.e("BootReceiver", "Failed to reschedule auto backup on boot", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
