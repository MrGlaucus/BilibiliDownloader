package com.glaucus.bilibilidownloader.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.glaucus.bilibilidownloader.R
import com.glaucus.bilibilidownloader.entity.Part
import com.glaucus.bilibilidownloader.service.ConvertService
import com.glaucus.bilibilidownloader.util.CONVERT_ACTION
import kotlinx.android.synthetic.main.item_part.view.*

/**
 * Created by glaucus on 2018/2/26.
 */
class PartAdapter(private val context: Context, private val items: List<Part>) : RecyclerView.Adapter<PartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false)
        return ViewHolder(context, view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(private val context: Context, private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(part: Part) {
            view.part_order.text = part.pageData.part
            view.part_title.text = part.title
            view.convert.setOnClickListener {
                val intent = Intent(context, ConvertService::class.java)
                intent.putExtra("path", part.path)
                intent.putExtra("title", part.title)
                intent.putExtra("cid", part.pageData.cid)
                intent.action = CONVERT_ACTION
                context.startService(intent)
            }
        }
    }
}