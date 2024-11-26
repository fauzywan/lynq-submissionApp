package com.example.lynq.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lynq.R
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.ui.DetailStoryActivity

class StoryAdapter(private val stories: List<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val photo: ImageView = view.findViewById(R.id.iv_item_photo)
        val name: TextView = view.findViewById(R.id.tv_item_name)
        val description: TextView = view.findViewById(R.id.tvItemDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.name.text = story.name
        holder.description.text = story.description
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intent.putExtra("selected_story", story)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = stories.size
}
