package org.meetcute.appUtils

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<type> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val itemList = mutableListOf<type?>()

    fun get(position: Int): type? {
        return itemList[position]
    }


    fun clear(){
        itemList.clear()
        notifyDataSetChanged()
    }
    fun getAll(): MutableList<type?> = itemList

    fun add(item: type?) {
        val count = itemList.count()
        itemList.add(item)
        notifyItemInserted(count)
    }

    fun add(item: List<type?>) {
        val count = itemList.count()
        itemList.addAll(item)
        notifyItemRangeInserted(count,item.count())
    }

    fun set(list: List<type?>) {
        itemList.clear()
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    fun changeItemAt(url: type?, position: Int) {
        if (itemList.size >= (position - 1)) {
            itemList[position] = url
            notifyItemChanged(position)
        }
    }

    fun removeAt(position: Int) {
        try {
            itemList.removeAt(position)
            notifyItemRemoved(position)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount() = itemList.size

}