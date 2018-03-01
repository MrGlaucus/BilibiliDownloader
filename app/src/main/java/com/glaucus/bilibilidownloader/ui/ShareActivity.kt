package com.glaucus.bilibilidownloader.ui

import VideoHandle.EpVideo
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.glaucus.bilibilidownloader.R
import com.glaucus.bilibilidownloader.adapter.PartAdapter
import com.glaucus.bilibilidownloader.entity.Part
import com.glaucus.bilibilidownloader.service.ConvertService
import com.glaucus.bilibilidownloader.util.*
import kotlinx.android.synthetic.main.activity_share.*
import permissions.dispatcher.*
import java.io.File

@RuntimePermissions
class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        processShareTextWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun processShareText() {
        val action = intent.action
        val type = intent.type
        if (action == Intent.ACTION_SEND) {
            if (type == "text/plain") {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val av = text.parseAV()
                if (av.isEmpty()) {
                    toast("解析AV号失败，请检查您的分享是否正确")
                    finish()
                } else {
                    val list = getLocalPartsByAv(av)
                    when {
                        list.isEmpty() -> {
                            toast("缓存数据获取失败，请检查缓存文件和文件读写权限")
                            finish()
                        }
                        list.size == 1 -> {
                            //单P则直接开始转换
                            val part = list.first()
                            val intent = Intent(this, ConvertService::class.java)
                            intent.putExtra("path", part.path)
                            intent.putExtra("title", part.title)
                            intent.putExtra("cid", part.pageData.cid)
                            intent.action = CONVERT_ACTION
                            toast("降维任务启动！")
                            startService(intent)
                            finish()
                        }
                        else -> {
                            val adapter = PartAdapter(this, list)
                            val layoutManager = LinearLayoutManager(this)
                            recyclerView.layoutManager = layoutManager
                            recyclerView.adapter = adapter
                            progressBar.gone()
                        }
                    }
                }
            }
        }
    }

    private fun getLocalPartsByAv(av: String): List<Part> {
        //获取BILI缓存下载路径
        val dir = File(BILI_DOWNLOAD_PATH)
        //判断路径是否存在或AV号是否存在缓存
        return if (!dir.exists() || !dir.listFiles().any { it.name == av }) {
            emptyList()
        } else {
            //获取该AV号的目录
            val avDir = File(dir.absolutePath + separator + av)
            //获取分P目录
            val parts = avDir.listFiles()
            //获取分P目录下的entry.json文件内容，并将其转换为实体对象
            parts.map { File(it.absolutePath + separator + "entry.json").readText().parsePart(it.absolutePath) }
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showDeniedForCamera() {
        toast("本程序需要文件存储权限")
        finish()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showNeverAskForCamera() {
        toast("本程序需要文件存储权限")
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}