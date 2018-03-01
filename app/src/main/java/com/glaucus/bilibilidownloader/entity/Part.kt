package com.glaucus.bilibilidownloader.entity

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.glaucus.bilibilidownloader.deserializer.PartDeserializer

@JsonDeserialize(using = PartDeserializer::class)
data class Part(var path: String = "", val typeTag: String, val title: String,
                val isCompleted: Boolean, val avid: Int, val pageData: PageData)