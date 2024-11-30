package com.example.lynq.ui.story

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.adapter.StoryAdapter
import com.example.lynq.data.Result
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.databinding.ActivityStoryBinding
import com.example.lynq.ui.post.PostStoryActivity
import com.example.lynq.ui.settings.SettingsActivity
import com.example.lynq.utils.ThemeisDark

class StoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setupView()
        setupAction()
        setContentView(binding.root)
        viewModel.getSession()
        viewModel.userSession.observe(this){user->
            binding.tvname.text=user.name
        }
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

    private fun setupAction() {
        binding.btnAddStory.setOnClickListener{
            val intent = Intent(this, PostStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        supportActionBar?.hide()
        viewModel.darkMode.observe(this) { isDarkMode ->
          ThemeisDark(isDarkMode)
        }
        val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isDarkMode) {
            binding.logo.setImageResource(R.drawable.lynq_mini_night)
        } else {
            binding.logo.setImageResource(R.drawable.lynq_mini)
        }
        binding.settingButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView(stories: List<ListStoryItem>) {
        val adapter = StoryAdapter(stories)
        val orientation = resources.configuration.orientation

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels / displayMetrics.density
        val columnWidth = 360

        val spanCount = (screenWidth / columnWidth).toInt().coerceAtLeast(1)
        binding.rvStories.layoutManager = GridLayoutManager(this, spanCount)
        binding.rvStories.adapter = adapter
    }
}