package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackHistoryManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Const.TRACK_HISTORY, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveTrackToHistory(track: Track) {
        var trackHistory = loadTrackHistory()
        if (trackHistory.any { it.trackId == track.trackId }) {
            trackHistory.removeAll { it.trackId == track.trackId }
            Toast.makeText(context, "Трэк ${track.trackName} уже есть в истории", Toast.LENGTH_SHORT).show()
        }
        trackHistory.add(0, track)
        if (trackHistory.size > 10) {
            trackHistory = trackHistory.subList(0, 9)
        }
        saveTrackHistory(trackHistory)
    }


    fun loadTrackHistory(): MutableList<Track> {
        val trackHistoryJson = sharedPreferences.getString(Const.TRACK_HISTORY, "")
        return if (trackHistoryJson.isNullOrEmpty()) {
            mutableListOf()
        } else {
            gson.fromJson(trackHistoryJson, object : TypeToken<MutableList<Track>>() {}.type)
        }
    }

    private fun saveTrackHistory(trackHistory: MutableList<Track>) {
        val trackHistoryJson = gson.toJson(trackHistory)
        sharedPreferences.edit().putString(Const.TRACK_HISTORY, trackHistoryJson).apply()
    }

    fun clearTrackHistory() {
        val editor = sharedPreferences.edit()
        editor.remove(Const.TRACK_HISTORY)
        editor.apply()
    }
}