package com.example.lynq.ui.auth.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.ActivityRegisterBinding
import com.example.lynq.ui.auth.login.LoginActivity
import com.example.lynq.data.Result

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
            setupView()
            setupAction()

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
//                    binding.progressBar.visibility = View.GONE
                    AlertDialog.Builder(this).apply {
                        setTitle("Yeah!")
                        setMessage("Identitas Berhasil Dibuat,Pastikan Kamu Mengingatnya")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)

                            startActivity(intent)

                        }
                        create()
                        show()
                    }
                    Log.d("kak", "onCreate: ${result.data}")
                }
                is Result.Error -> {
//                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                         result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }

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

        private fun setupAction() {
            binding.login.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()

            }
            binding.signupButton.setOnClickListener {
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                Log.d("password", "setupAction: ${password}")
                if (password.length < 8) {
                    binding.passwordEditTextLayout.error = "Password harus memiliki minimal 8 karakter"
                } else {

                        viewModel.register(name,email, password)
                    }
            }
        }
}