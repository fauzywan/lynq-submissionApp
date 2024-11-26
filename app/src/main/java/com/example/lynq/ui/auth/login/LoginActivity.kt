package com.example.lynq.ui.auth.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lynq.MainActivity
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.ActivityLoginBinding
import com.example.lynq.data.Result
import com.example.lynq.ui.auth.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    viewModel.saveSession(user)

                    viewModel.sessionSaveResult.observe(this) { isSaved ->
                        if (isSaved) {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Hallo ${user.name}")
                                setMessage("Selamat Datang,Selamat Menjelajah")
                                setPositiveButton("Buka Gerbang") { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }
                                create()
                                show()
                            }
                        } else {
                            Toast.makeText(this, "Gagal menyimpan session", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    when {
                        result.error.contains("email") -> {
                            binding.edLoginEmail.error = result.error
                            binding.edLoginEmail.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.holo_red_dark))
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
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email,password)
        }
        binding.registrasion.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.edLoginPassword.isEnabled = s.isNotEmpty()
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()
                if(isEmailValid){
                    binding.edLoginEmail.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this@LoginActivity, android.R.color.darker_gray))

                }
                binding.loginButton.isEnabled = isEmailValid && binding.edLoginPassword.isEnabled
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(binding.edLoginEmail.text.toString()).matches()
                val isPasswordNotEmpty = s.toString().isNotEmpty()

                binding.loginButton.isEnabled = isEmailValid && isPasswordNotEmpty
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edLoginEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.edLoginEmail.text.toString().trim()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.edLoginEmail.error = "Invalid email format"
                } else {
                    binding.edLoginEmail.error = null
                }
            }else{
                binding.edLoginEmail.error = null
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