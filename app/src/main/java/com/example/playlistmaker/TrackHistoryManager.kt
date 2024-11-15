package com.example.playlistmaker

// TrackHistoryManager.kt
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackHistoryManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("track_history", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTrackToHistory(track: Track) {
        var trackHistory = loadTrackHistory()
        if (trackHistory.contains(track)) {
            trackHistory.remove(track)
            Toast.makeText(context, "Track removed from history", Toast.LENGTH_SHORT).show()
        }
        trackHistory.add(0, track)
        if (trackHistory.size > 10) {
            trackHistory = trackHistory.subList(0, 10)
        }
        saveTrackHistory(trackHistory)
    }

    fun loadTrackHistory(): MutableList<Track> {
        val trackHistoryJson = sharedPreferences.getString("track_history", "")
        return if (trackHistoryJson.isNullOrEmpty()) {
            mutableListOf()
        } else {
            gson.fromJson(trackHistoryJson, object : TypeToken<MutableList<Track>>() {}.type)
        }
    }

    private fun saveTrackHistory(trackHistory: MutableList<Track>) {
        val trackHistoryJson = gson.toJson(trackHistory)
        sharedPreferences.edit().putString("track_history", trackHistoryJson).apply()
    }
}