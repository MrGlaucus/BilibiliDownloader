package com.glaucus.bilibilidownloader.deserializer

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.glaucus.bilibilidownloader.entity.PageData
import com.glaucus.bilibilidownloader.entity.Part

/**
 * Created by glaucus on 2018/2/26.
 */
class PartDeserializer : JsonDeserializer<Part>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Part {
        val node = p.codec.readTree<JsonNode>(p)
        val typeTag = node.get("type_tag").asText()
        val title = node.get("title").asText()
        val isCompleted = node.get("is_completed").asBoolean()
        val avid = node.get("avid").asInt()
        val pageData = node.get("page_data")
        val part = pageData.get("part").asText()
        val cid = pageData.get("cid").asInt()
        return Part(typeTag = typeTag, title = title, isCompleted = isCompleted, avid = avid, pageData = PageData(part, cid))
    }
}