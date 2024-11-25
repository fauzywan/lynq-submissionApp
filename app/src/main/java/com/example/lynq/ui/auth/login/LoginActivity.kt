package com.example.lynq.ui.auth.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lynq.MainActivity
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.ActivityLoginBinding
import com.example.lynq.data.Result
import com.example.lynq.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val user = result.data
                    viewModel.saveSession(user).apply {
                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle("Hallo ${user.name}")
                            setMessage("Selamat Datang,Selamat Menjelajah")
                            setPositiveButton("Buka Gerbang") { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    when {

                        result.error.contains("email") -> {
                            binding.edRegisterEmailLayout.error = result.error
                        }

                        else -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }


            }
        }
    }



    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.login(email,password)
        }
        binding.registrasion.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.edRegisterEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.edRegisterEmail.text.toString().trim()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edRegisterEmailLayout.error = "Invalid email format"
                } else {
                    binding.edRegisterEmailLayout.error = null // Hapus error jika email valid
                }
            }else{
                binding.edRegisterEmailLayout.error = null
            }
        }
    }

    private fun setupView() {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.hide()
    }
}