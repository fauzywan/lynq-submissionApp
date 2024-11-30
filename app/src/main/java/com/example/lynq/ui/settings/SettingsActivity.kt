package com.example.lynq.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.lynq.R
import com.example.lynq.ViewModelFactory
import com.example.lynq.databinding.ActivityPostStoryBinding
import com.example.lynq.databinding.ActivitySettingsBinding
import com.example.lynq.ui.auth.login.LoginActivity
import com.example.lynq.ui.post.PostViewModel
import com.example.lynq.ui.story.StoryActivity
import com.example.lynq.utils.ThemeisDark
import com.loopj.android.http.AsyncHttpClient.log
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: SettingsViewModel by viewModels { factory }
        viewModel.darkMode.observe(this) { isDarkMode ->
            ThemeisDark(isDarkMode)
            binding.darkModeSwitch.isChecked = isDarkMode
            binding.darkmodeText.text = if (!isDarkMode) "Light Mode" else "Dark Mode"
        }
        binding.mainAppBar.setOnClickListener{
            val intent = Intent(this, StoryActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        binding.settingLanguageArrow.setOnClickListener{
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.darkModeSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.toggleDarkMode(isChecked)
        }
        val locale = Locale.getDefault()
        binding.language.text = "${locale.displayLanguage} ${ifSameLangCountry(locale)}"
        binding.actionLogout.setOnClickListener{
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    viewModel.logout().apply {
                        val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
    private fun setupView() {

        supportActionBar?.hide()
    }

    private fun ifSameLangCountry(locale: Locale): String {
        if(locale.displayCountry!=locale.displayLanguage){
            return "(${locale.displayCountry})"
        }
        return ""
    }
}