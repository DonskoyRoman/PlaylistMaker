package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        loadTheme()
    }
    private fun loadTheme() {
        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        darkTheme = prefs.getBoolean("dark_theme", false)
        setTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val prefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean("dark_theme", darkTheme)
            apply()
        }

        setTheme(darkThemeEnabled)
    }
    private fun setTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
