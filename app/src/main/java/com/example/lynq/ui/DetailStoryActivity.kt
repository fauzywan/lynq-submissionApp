package com.example.lynq.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.lynq.R
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.databinding.ActivityDetailStoryBinding
import com.example.lynq.databinding.ActivityMainBinding

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