package com.example.lynq

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.lynq.databinding.ActivityMainBinding
import com.example.lynq.ui.auth.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    override fun onStart() {
        super.onStart()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
        viewModel.getSession()
        viewModel.userSession.observe(this) { user ->
            if (user.name.isNotEmpty()) {


                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)


                val navView: BottomNavigationView = binding.navView
                val navController = findNavController(R.id.nav_host_fragment_activity_main)
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)
                navView.setupWithNavController(navController)
                Toast.makeText(this, "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}