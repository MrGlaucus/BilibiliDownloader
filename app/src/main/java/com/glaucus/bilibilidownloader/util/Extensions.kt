package com.glaucus.bilibilidownloader.util

import VideoHandle.EpEditor
import VideoHandle.OnEditorListener
import android.app.Activity
import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.glaucus.bilibilidownloader.entity.Part
import java.io.File

/**
 * Created by glaucus on 2018/2/25.
 */
fun String.parseAV(): String {
    val avPattern = """av\d+""".toRegex()
    return avPattern.find(this)?.value?.replace("av", "") ?: ""
}

fun String.parsePart(partPath: String): Part {
    val mapper = ObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false)
    val part = mapper.readValue(this, Part::class.java)
    part.path = File(partPath + separator + part.typeTag).listFiles().find { it.extension == "blv" }!!.absolutePath
    Log.d(TAG, part.toString())
    return part
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}