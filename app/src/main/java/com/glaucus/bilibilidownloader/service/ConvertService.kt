package com.glaucus.bilibilidownloader.service

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import android.app.*
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.glaucus.bilibilidownloader.R
import com.glaucus.bilibilidownloader.util.CONVERT_ACTION
import com.glaucus.bilibilidownloader.util.TAG
import com.glaucus.bilibilidownloader.util.separator
import java.io.File

/**
 * Created by glaucus on 2018/2/26.
 */
class ConvertService : Service() {
    lateinit var mNotifyManager: NotificationManager
    lateinit var mBuilder: Notification.Builder
    var id = 1
    override fun onCreate() {
        mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = Notification.Builder(applicationContext)
        mBuilder.setContentText("正在降维...")
                .setSmallIcon(R.mipmap.ic_launcher)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == CONVERT_ACTION) {
            val title = intent.getStringExtra("title")
            val path = intent.getStringExtra("path")
            id = intent.getIntExtra("cid", 1)
            val OUTPUT_PATH = getExternalFilesDir(Environment.DIRECTORY_MUSIC).absolutePath
            if (!File(OUTPUT_PATH).exists()) {
                File(OUTPUT_PATH).mkdirs()
            }
            mBuilder.setContentText("正在降维-$title")
            mBuilder.setProgress(100, 0, true)
            mNotifyManager.notify(id, mBuilder.build())
            EpEditor.demuxer(path, OUTPUT_PATH + separator + title + ".mp3", EpEditor.Format.MP3, object : OnEditorListener {
                override fun onSuccess() {
                    mBuilder.setContentText("降维成功-$title")
                    mBuilder.setProgress(0, 0, false)
                    val jumpIntent = Intent()
                    jumpIntent.action = Intent.ACTION_VIEW
                    jumpIntent.setDataAndType(Uri.parse(OUTPUT_PATH + separator + title + ".mp3"), "audio/*")
                    mBuilder.setContentIntent(PendingIntent.getActivity(applicationContext, 0, jumpIntent, 0))
                    mNotifyManager.notify(id, mBuilder.build())
                    stopSelf()
                }

                override fun onFailure() {
                    mBuilder.setContentText("降维失败-$title")
                    mBuilder.setProgress(0, 0, false)
                    mNotifyManager.notify(id, mBuilder.build())
                    stopSelf()
                }

                override fun onProgress(p0: Float) {
                    mBuilder.setProgress(100, (p0 * 100).toInt(), true)
                    mBuilder.setContentText(title)
                    mNotifyManager.notify(id, mBuilder.build())
                }
            })
        }
        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service Destroyed")
    }
}