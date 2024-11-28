package com.example.lynq.ui.story

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.adapter.StoryAdapter
import com.example.lynq.data.Result
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.databinding.ActivityLoginBinding
import com.example.lynq.databinding.ActivityStoryBinding
import com.example.lynq.ui.auth.login.LoginViewModel

class StoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getAllStory.observe(this){result->
            when(result){
                is Result.Loading->{
                    binding.progressBar.visibility= View.VISIBLE
                }
                is Result.Success->{
                    val data = result.data
                    binding.progressBar.visibility= View.GONE
                    setupRecyclerView(data.listStory)

                }
                is Result.Error->{
                    binding.progressBar.visibility= View.GONE
                    Toast.makeText(this,result.error, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
    private fun setupRecyclerView(stories: List<ListStoryItem>) {
        val adapter = StoryAdapter(stories)
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.adapter = adapter
    }
}