package com.jawabdulu.app.services

import android.app.*
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.jawabdulu.app.preferences.Preferences
import com.jawabdulu.app.ui.activity.SplashJawabDuluActivity
import java.util.*

class BackgroundServiceKt : Service() {
    var mContext: Context? = null
//    var db: Apply_password_on_appDatabase = Apply_password_on_appDatabase(this)
    var current_app = ""
    override fun onCreate() {
        super.onCreate()
        mContext = this
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startMyOwnForeround() else startForeground(
            1,
            Notification()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeround() {
        Log.d(TAG, "startMyOwnForeround: start")
        val NOTIFICATION_CHANNEL_ID = "example.permanence"
        val channelName = "Background Service"
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(channel)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stoptimertask()
        val broadcastIntent = Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.setClass(this, RestartServiceKt::class.java)
        this.sendBroadcast(broadcastIntent)
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    fun startTimer() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                if (flag == 0) {
                    val name = Preferences.getListPackageName()

                    if (name.contains(printForegroundTask())) {
                        flag = 1
                        current_app = printForegroundTask()
                        val lockIntent = Intent(mContext, SplashJawabDuluActivity::class.java)
//                        lockIntent.putExtra("name", name.indexOf(printForegroundTask()))
//                        lockIntent.putExtra("pack", printForegroundTask())
                        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        //                        lockIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext!!.startActivity(lockIntent)
                    }
                }
                if (printForegroundTask() != "web.id.azammukhtar.peka" && flag2 == 0) {
                    if (printForegroundTask() != current_app) {
                        flag = 0
                    }
                }

                if (printForegroundTask() == "web.id.azammukhtar.peka") {
                    flag = 2
                }
            }
        }
        timer!!.schedule(timerTask, 0, 100)
    }

    fun stoptimertask() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun printForegroundTask(): String {
        var currentApp = "NULL"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val usageStatsManager = this.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val appList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 1000,
                time
            )
            if (appList != null && appList.size > 0) {
                val sortedMap: SortedMap<Long, UsageStats> = TreeMap()
                for (usageStats in appList) {
                    sortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (sortedMap != null && !sortedMap.isEmpty()) {
                    currentApp = sortedMap[sortedMap.lastKey()]!!.packageName
                }
            }
        } else {
            val activityManager = this.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val tasks = activityManager.runningAppProcesses
            currentApp = tasks[0].processName
        }
        return currentApp
    }

    companion object {
        private const val TAG = "BackgroundService"
        var flag = 0
        var flag2 = 0
    }
}