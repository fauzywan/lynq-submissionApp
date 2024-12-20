package com.example.lynq.ui.auth.login

import com.example.lynq.R
import android.content.Intent
import android.content.res.Configuration
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
import com.example.lynq.MainActivity
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.ActivityLoginBinding
import com.example.lynq.data.Result
import com.example.lynq.ui.auth.register.RegisterActivity
import com.example.lynq.ui.story.StoryActivity
import com.example.lynq.utils.ThemeisDark

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
        viewModel.getSession()
        viewModel.userSession.observe(this) { user ->
            if (user.name.isNotEmpty()) {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    enabledElement(true)
                    binding.progressBar.visibility = View.GONE
                    val user = result.data
                    viewModel.saveSession(user)

                    viewModel.sessionSaveResult.observe(this) { isSaved ->
                        if (isSaved) {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(getString(com.example.lynq.R.string.hallo, user.name))
                                setMessage(getString(com.example.lynq.R.string.welcome_traveler))
                                setPositiveButton(getString(com.example.lynq.R.string.open_gate)) { _, _ ->
                                    val intent = Intent(context, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            Toast.makeText(this,
                                getString(com.example.lynq.R.string.faile_session), Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Result.Error -> {
                    enabledElement(true)
                    binding.progressBar.visibility = View.GONE
                    when {
                        result.error.contains("email") -> {
                            binding.edLoginEmail.error = result.error
                        }

                        else -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                is Result.Loading -> {
                    enabledElement(false)

                    binding.progressBar.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun enabledElement(isEnabled: Boolean) {
        binding.loginButton.isEnabled=isEnabled
        binding.edLoginEmail.isEnabled=isEnabled
        binding.edLoginPassword.isEnabled=isEnabled
        binding.registrasion.isEnabled=isEnabled
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
                binding.loginButton.isEnabled = isEmailValid && passwordValidation()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(binding.edLoginEmail.text.toString()).matches()

                binding.loginButton.isEnabled = isEmailValid && passwordValidation()
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
    private fun passwordValidation(): Boolean {
        return (binding.edLoginPassword.text.toString().isNotEmpty()&&binding.edLoginPassword.text.toString().length>8)
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
        viewModel.darkMode.observe(this) { isDarkMode ->
            ThemeisDark(isDarkMode)
        }
            supportActionBar?.hide()
        val isDarkMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        if (isDarkMode) {
            binding.imageView.setImageResource(R.drawable.lynq_night)
        } else {
            binding.imageView.setImageResource(R.drawable.lynq)
        }
    }

}