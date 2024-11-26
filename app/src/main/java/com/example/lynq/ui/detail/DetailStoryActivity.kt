package com.example.lynq.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.databinding.ActivityDetailStoryBinding

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedStory: ListStoryItem? = intent.getParcelableExtra("selected_story")

        if(selectedStory != null){
            Glide.with(this)
            .load(selectedStory.photoUrl)
            .into(binding.ivDetailPhoto)
            binding.tvDetailName.text = selectedStory.name
            binding.tvDetailDescription.text = selectedStory.description

        }

        binding.btnBack.setOnClickListener {
            onBackPressed()

        }
    }
}