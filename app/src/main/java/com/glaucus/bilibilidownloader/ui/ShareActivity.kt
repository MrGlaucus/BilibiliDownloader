package com.glaucus.bilibilidownloader.ui

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.glaucus.bilibilidownloader.R
import com.glaucus.bilibilidownloader.adapter.PartAdapter
import com.glaucus.bilibilidownloader.entity.Part
import com.glaucus.bilibilidownloader.service.ConvertService
import com.glaucus.bilibilidownloader.util.*
import kotlinx.android.synthetic.main.activity_share.*
import kotlinx.android.synthetic.main.item_part.*
import java.io.File

class ShareActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        val action = intent.action
        val type = intent.type
        if (action == Intent.ACTION_SEND) {
            if (type == "text/plain") {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val av = text.parseAV()
                if (av.isEmpty()) {
                    toast("解析AV号失败，请检查您的分享是否正确")
                    finish()
                    return
                } else {
                    val list = getLocalPartsByAv(av)
                    if (list.isEmpty()) {
                        toast("缓存数据获取失败，请确认该视频是否已缓存")
                        finish()
                    } else if (list.size == 1) {
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
                    } else {
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

    private fun getLocalPartsByAv(av: String): List<Part> {
        //获取BILI缓存下载路径
        val dir = File(BILI_DOWNLOAD_PATH)
        //判断路径是否存在或AV号是否存在缓存
        if (!dir.exists() || !dir.listFiles().any { it.name == av }) {
            return arrayListOf()
        } else {
            //获取该AV号的目录
            val avDir = File(dir.absolutePath + separator + av)
            //获取分P目录
            val parts = avDir.listFiles()
            //获取分P目录下的entry.json文件内容，并将其转换为实体对象
            return parts.map { File(it.absolutePath + separator + "entry.json").readText().parsePart(it.absolutePath) }
        }
    }
}