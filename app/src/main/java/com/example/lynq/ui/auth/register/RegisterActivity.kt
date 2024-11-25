package com.example.lynq.ui.auth.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
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

        binding.edRegisterName.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
        if(binding.edRegisterNameLayout.error!=null)
        {
            binding.edRegisterNameLayout
                .error=null
        }
        }
        binding.edRegisterEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(binding.edRegisterEmailLayout.error!=null)
            {
                binding.edRegisterEmailLayout.error=null
            }
        }
        binding.edRegisterEmail.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val email = binding.edRegisterEmail.text.toString().trim()
                if (!isValidEmail(email)) {
                    binding.edRegisterEmailLayout.error = "Invalid email format"
                } else {
                    binding.edRegisterEmailLayout.error = null // Hapus error jika email valid
                }
            }else{
                    binding.edRegisterEmailLayout.error = null
            }
        }
        viewModel.registerResult.observe(this) { result ->
            when (result) {

                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.edRegisterNameLayout.error = null
                    binding.progressBar.visibility = View.GONE
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
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    when {
                        result.error.contains("name") -> {
                            binding.edRegisterNameLayout.error =result.error
                        }
                        result.error.contains("email") -> {
                            binding.edRegisterEmailLayout.error =result.error
                        }

                        else -> {
                            binding.edRegisterNameLayout.error = result.error
                        }
                    }
                }
            }
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
//
//            binding.edRegisterEmail.On = View.OnFocusChangeListener { _, hasFocus ->
//                if(binding.edRegisterNameLayout.error!=null)
//                {
//                    binding.edRegisterNameLayout
//                        .error=null
//                }
//            }

            binding.signupButton.setOnClickListener {
                val name = binding.edRegisterName.text.toString()
                val email = binding.edRegisterEmail.text.toString()
                val password = binding.edRegisterPassword.text.toString()
                Log.d("password", "setupAction: ${password}")
                if (password.length < 8) {
                    binding.edRegisterPasswordLayout.error = "Password harus memiliki minimal 8 karakter"
                } else {
                    binding.edRegisterPasswordLayout.error=null
                    viewModel.register(name,email, password)
                    }
            }
        }
}