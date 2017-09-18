package com.zalologin.ebook

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zalologin.R
import com.zalologin.databinding.ItemChapterBinding

/**
 * //Todo
 *
 * Created by HOME on 9/14/2017.
 */
class ListBookAdapter(var context: Context, var chapters: List<String>, var listener : View.OnClickListener) : RecyclerView.Adapter<ListBookAdapter.ItemViewHolder>() {

    override fun getItemCount(): Int = chapters.size

    override fun onBindViewHolder(holder: ListBookAdapter.ItemViewHolder?, position: Int) {
        holder?.bind(chapters[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListBookAdapter.ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false)
        return ItemViewHolder(view, listener)
    }

    class ItemViewHolder(view: View, listener: View.OnClickListener) : RecyclerView.ViewHolder(view) {
        var binding: ItemChapterBinding
        var listener : View.OnClickListener

        init {
            binding = DataBindingUtil.bind(view)
            this.listener = listener
        }

        fun bind(name: String, position: Int) {
            binding.tvChapter.tag = position
            binding.chapter = name
            binding.tvChapter.setOnClickListener(listener)
        }
    }

}