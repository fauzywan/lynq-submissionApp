package com.example.lynq.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.databinding.ActivityDetailStoryBinding
import com.example.lynq.ui.settings.SettingsActivity
import com.example.lynq.ui.story.StoryActivity

@Suppress("DEPRECATION")
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
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
            val intent = Intent(this, StoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}