package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.playlistmaker.TrackHistoryManager

class SearchActivity : AppCompatActivity() {
    private lateinit var queryInput: EditText
    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var imageNoResultsError: LinearLayout
    private lateinit var imageNetworkError: LinearLayout
    private lateinit var clearIcon: ImageView
    private lateinit var refreshButton: Button
    private val tracks = ArrayList<Track>()

    private val adapter = TrackAdapter(tracks) { track ->
        // Обработчик кликов, например, открытие новой активности с детальной информацией о песне
//        showTrackDetails(track)
        val trackHistoryManager=TrackHistoryManager(this)
        trackHistoryManager.saveTrackToHistory(track)
        Toast.makeText(this, "Track added to history: ${track.trackName}", Toast.LENGTH_SHORT).show()
    }
    private val trackHistoryAdapter = TrackAdapter(ArrayList()) { track ->
        // Логика добавления трека в начало истории
        val trackHistoryManager = TrackHistoryManager(this)

        // Сохраняем трек в историю (в начало списка)
        trackHistoryManager.saveTrackToHistory(track)

        // Перезагружаем историю
        showTrackHistory()  // Это обновит данные в истории

        Toast.makeText(this, "Track added to history: ${track.trackName}", Toast.LENGTH_SHORT).show()
    }


    private val api: ITunesApi = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        queryInput = findViewById(R.id.inputEditText)
        trackListRecyclerView = findViewById(R.id.trackListRecyclerView)
        historyRecyclerView = findViewById(R.id.historyRecyclerView)
        imageNoResultsError = findViewById(R.id.imageNoResultsErrorXML)
        imageNetworkError = findViewById(R.id.imageNetworkErrorXML)
        clearIcon = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)

        trackListRecyclerView.layoutManager = LinearLayoutManager(this)
        trackListRecyclerView.adapter = adapter

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = trackHistoryAdapter

        clearIcon.setOnClickListener {
            queryInput.text.clear()
            clearIcon.visibility = View.GONE
            showTrackHistory()  // Показываем историю, когда очищен запрос
        }

//        val trackHistoryManager = TrackHistoryManager(this)

        // Изначально показываем историю
        showTrackHistory()

        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotEmpty()) {
                    searchTracks(queryInput.text.toString())
                } else {
                    showTrackHistory()
                }
                hideKeyboard()
                true
            } else {
                false
            }
        }

        refreshButton.setOnClickListener { onRefreshClick() }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
    }

    private fun showTrackHistory() {
        val trackHistoryManager = TrackHistoryManager(this)
        val trackHistory = trackHistoryManager.loadTrackHistory()

        // Управляем видимостью двух RecyclerView
        if (trackHistory.isEmpty()) {
            historyRecyclerView.visibility = View.GONE
            imageNoResultsError.visibility = View.VISIBLE
            trackListRecyclerView.visibility = View.GONE
        } else {
            historyRecyclerView.visibility = View.VISIBLE
            trackListRecyclerView.visibility = View.GONE
            imageNoResultsError.visibility = View.GONE

            // Обновляем список треков в адаптере
            trackHistoryAdapter.setTracks(trackHistory)
        }

        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }


    private fun searchTracks(query: String) {
        tracks.clear()
        adapter.notifyDataSetChanged()

        // Управляем видимостью при поиске
        trackListRecyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.GONE

        api.searchTracks(query).enqueue(object : Callback<TrackSearchResponse> {
            override fun onResponse(
                call: Call<TrackSearchResponse>,
                response: Response<TrackSearchResponse>
            ) {
                when (response.code()) {
                    200 -> {
                        val trackSearchResponse = response.body()
                        if (trackSearchResponse != null && trackSearchResponse.resultCount > 0) {
                            tracks.addAll(trackSearchResponse.results)
                            adapter.notifyDataSetChanged()
                            showTracks()
                        } else {
                            showNoResultsErrorSearchActivity()
                        }
                    }
                    else -> showNetworkErrorSearchActivity("Ошибка сети")
                }
            }

            override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                showNetworkErrorSearchActivity("Ошибка сети")
            }
        })
    }

    private fun showTrackDetails(track: Track) {
        // Логика для открытия экрана с подробной информацией о песне
        Toast.makeText(this, "Track clicked: ${track.trackName}", Toast.LENGTH_SHORT).show()
    }


    private fun showTracks() {
        trackListRecyclerView.visibility = View.VISIBLE
        imageNoResultsError.visibility = View.GONE
        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showNoResultsErrorSearchActivity() {
        trackListRecyclerView.visibility = View.GONE
        imageNoResultsError.visibility = View.VISIBLE
        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }

    private fun showNetworkErrorSearchActivity(message: String) {
        trackListRecyclerView.visibility = View.GONE
        imageNoResultsError.visibility = View.GONE
        imageNetworkError.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun onRefreshClick() {
        val query = queryInput.text.toString()
        if (query.isNotEmpty()) {
            hideKeyboard()
            searchTracks(query)
        }
    }
}
