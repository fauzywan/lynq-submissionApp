package com.example.lynq.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lynq.ViewModelFactory
import com.example.lynq.adapter.StoryAdapter
import com.example.lynq.data.pref.UserModel
import com.example.lynq.databinding.FragmentHomeBinding
import com.example.lynq.ui.auth.login.LoginViewModel
import com.example.lynq.ui.dashboard.DashboardViewModel
import com.example.lynq.data.Result
import com.example.lynq.data.remote.response.ListStoryItem
import com.example.lynq.ui.auth.login.LoginActivity


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSession()
        viewModel.userSession.observe(requireActivity()) { user ->
            if (user != null) {
                Log.d("gggg", "onViewCreated:${user.token} ")
                viewModel.getAllStory(user.token)
            }
        }
        viewModel.stories.observe(viewLifecycleOwner){result->
            when(result){
                is Result.Loading->{
                    binding.progressBar.visibility=View.VISIBLE
                }
                is Result.Success->{
                    val data = result.data
                    binding.progressBar.visibility=View.GONE
                    setupRecyclerView(data.listStory)

                }
                is Result.Error->{
                    binding.progressBar.visibility=View.GONE
                    Toast.makeText(requireContext(),result.error,Toast.LENGTH_SHORT).show()
                }
            }
        }
/*     */
    }
    private fun setupRecyclerView(stories: List<ListStoryItem>) {
        val adapter = StoryAdapter(stories)
        binding.rvStories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStories.adapter = adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}