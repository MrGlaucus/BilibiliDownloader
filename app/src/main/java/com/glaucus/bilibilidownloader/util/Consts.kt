package com.glaucus.bilibilidownloader.util

import android.os.Build
import android.os.Environment
import java.io.File

/**
 * Created by glaucus on 2018/2/25.
 */
const val TAG = "BiliBiliDownloader"
const val BILI_PACKAGE = "tv.danmaku.bili"
const val STORAGE_PERMISSION_REQUEST_CODE = 666
val separator = File.separator!!
val BILI_DOWNLOAD_PATH = Environment.getExternalStorageDirectory().absolutePath + separator + "Android${separator}data" + separator + BILI_PACKAGE + separator + "download"
const val CONVERT_ACTION = "convert"