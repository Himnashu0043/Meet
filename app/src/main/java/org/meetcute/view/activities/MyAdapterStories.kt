package org.meetcute.view.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.meetcute.R

class MyAdapterStories(private val mList: List<Stories_Model>):RecyclerView.Adapter<MyAdapterStories.MyStoriesHolder> (){

    inner class MyStoriesHolder(item: View):RecyclerView.ViewHolder(item){
        val imageView: ImageView = item.findViewById(R.id.ivStories)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyStoriesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_stories,parent, false)
        return MyStoriesHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyStoriesHolder, position: Int) {
        val  itemsViewModel = mList[position]
        holder.imageView.setImageResource(itemsViewModel.image)
    }
}