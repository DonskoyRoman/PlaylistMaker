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
        val prefs = getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE)
        darkTheme = prefs.getBoolean(Const.KEY_DARK_THEME, false)
        setTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val prefs = getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE)
        with(prefs.edit()) {
            putBoolean(Const.KEY_DARK_THEME, darkTheme)
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
