package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val shareButton = findViewById<TextView>(R.id.button_share)
        val supportButton = findViewById<TextView>(R.id.button_support)
        val agreementButton = findViewById<TextView>(R.id.button_user_agreement)
        val backButton = findViewById<ImageView>(R.id.back_button)

        backButton.setOnClickListener {
            finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val prefs = getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean(Const.KEY_DARK_THEME, false)
        themeSwitcher.isChecked = isDarkTheme

        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }

        supportButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.developer_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
            }
            startActivity(emailIntent)
        }

        agreementButton.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.user_agreement_link)))
            startActivity(browserIntent)
        }
    }
}

