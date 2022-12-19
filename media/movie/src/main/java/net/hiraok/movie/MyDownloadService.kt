package net.hiraok.movie

import android.app.*
import android.content.Context
import android.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadManager.Listener
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import java.io.File

@UnstableApi
class MyDownloadService : DownloadService(1) {

    val downloadNotificationHelper = DownloadNotificationHelper(applicationContext, "download")

    private val listener = object : Listener {

        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: java.lang.Exception?
        ) {
            when (download.state) {
                Download.STATE_DOWNLOADING -> {
                    Log.i("download", "download state")
                }

                Download.STATE_COMPLETED -> {
                    val intent = TaskStackBuilder.create(applicationContext).run {
                        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                    }
                    downloadNotificationHelper.buildDownloadCompletedNotification(
                        applicationContext,
                        android.R.drawable.ic_dialog_alert,
                        intent,
                        "download Complete"
                    )
                    Log.i("download", "download complete")
                }
                Download.STATE_FAILED -> {
                    val intent = TaskStackBuilder.create(applicationContext).run {
                        getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
                    }
                    downloadNotificationHelper.buildDownloadCompletedNotification(
                        applicationContext,
                        android.R.drawable.ic_dialog_alert,
                        intent,
                        "download Fail"
                    )
                    Log.i("download", "download fail")
                }
                Download.STATE_STOPPED -> {
                    Log.i("download", "download stopped")

                }
                Download.STATE_RESTARTING -> {
                    Log.i("download", "download restrt")

                }
                Download.STATE_QUEUED -> {
                    Log.i("download", "download queue")

                }
                Download.STATE_REMOVING -> {
                    Log.i("download", "download removing")

                }
            }
        }
    }

    override fun getDownloadManager(): DownloadManager {
        val databaseProvider = StandaloneDatabaseProvider(applicationContext)
        val downloadCache = SimpleCache(
            File("${this.getExternalFilesDir(null)?.path}/cache"),
            NoOpCacheEvictor(),
            databaseProvider
        )
        val dataSourceFactory = DefaultHttpDataSource.Factory()
        val downloadExecutor = Runnable::run
        val downloadManager = DownloadManager(
            applicationContext,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )
        downloadManager.addListener(listener)
        return downloadManager
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(applicationContext, 0)
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (notificationManager.getNotificationChannel("download") == null) {
            val notificationChannel = NotificationChannel(
                "download",
                "再生準備",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = Notification.Builder(this, "download").apply {
            setContentTitle("再生準備")
            setContentText(downloads.size.toString())
            setSmallIcon(android.R.drawable.stat_sys_download)
        }
        return notification.build()
    }

}