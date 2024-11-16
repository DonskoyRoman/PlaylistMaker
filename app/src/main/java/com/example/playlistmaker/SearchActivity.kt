package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class SearchActivity : AppCompatActivity() {
    private lateinit var queryInput: EditText
    private lateinit var trackListRecyclerView: RecyclerView
    private lateinit var historyRecyclerView: RecyclerView
    private lateinit var imageNoResultsError: LinearLayout
    private lateinit var imageNetworkError: LinearLayout
    private lateinit var clearIcon: ImageView
    private lateinit var refreshButton: Button
    private lateinit var searchHistoryTitle: TextView
    private lateinit var clearHistoryButton: Button

    private val tracks = ArrayList<Track>()

    private val adapter = TrackAdapter(tracks) { track ->
        val trackHistoryManager = TrackHistoryManager(this)
        trackHistoryManager.saveTrackToHistory(track)
        Toast.makeText(this, "Трэк добавлен в историю: ${track.trackName}", Toast.LENGTH_SHORT)
            .show()
    }
    private val trackHistoryAdapter = TrackAdapter(ArrayList()) { track ->

        val trackHistoryManager = TrackHistoryManager(this)

        trackHistoryManager.saveTrackToHistory(track)

        showTrackHistory()

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
        searchHistoryTitle = findViewById(R.id.searchHistoryTitle)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        trackListRecyclerView.layoutManager = LinearLayoutManager(this)
        trackListRecyclerView.adapter = adapter

        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = trackHistoryAdapter

        clearIcon.setOnClickListener {
            queryInput.text.clear()
            clearIcon.visibility = View.GONE
            showTrackHistory()
        }

        clearHistoryButton.setOnClickListener {

            val trackHistoryManager = TrackHistoryManager(this)
            trackHistoryManager.clearTrackHistory()
            Toast.makeText(this, "История очищена", Toast.LENGTH_SHORT).show()
            showTrackHistory()
        }

        showTrackHistory()

        queryInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    clearIcon.visibility = View.GONE
                } else {
                    clearIcon.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

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

        if (trackHistory.isEmpty()) {
            historyRecyclerView.visibility = View.GONE
            imageNoResultsError.visibility = View.VISIBLE
            trackListRecyclerView.visibility = View.GONE
            searchHistoryTitle.visibility = View.GONE
            clearHistoryButton.visibility = View.GONE


        } else {
            searchHistoryTitle.visibility = View.VISIBLE
            historyRecyclerView.visibility = View.VISIBLE
            trackListRecyclerView.visibility = View.GONE
            imageNoResultsError.visibility = View.GONE
            clearHistoryButton.visibility = View.VISIBLE


            trackHistoryAdapter.setTracks(trackHistory)
        }

        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
    }


    private fun searchTracks(query: String) {
        tracks.clear()
        adapter.notifyDataSetChanged()

        trackListRecyclerView.visibility = View.VISIBLE
        historyRecyclerView.visibility = View.GONE
        searchHistoryTitle.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE

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

                    400 -> showNetworkErrorSearchActivity(R.string.error400.toString())
                    403 -> showNetworkErrorSearchActivity(R.string.error403.toString())
                    500 -> showNetworkErrorSearchActivity(R.string.error500.toString())
                    else -> showNetworkErrorSearchActivity(R.string.errorUnknown.toString())
                }
            }

            override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                showNetworkErrorSearchActivity(R.string.errorNetwork.toString())
            }
        })
    }

    private fun showTracks() {
        trackListRecyclerView.visibility = View.VISIBLE
        imageNoResultsError.visibility = View.GONE
        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
        searchHistoryTitle.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE


    }

    private fun showNoResultsErrorSearchActivity() {
        trackListRecyclerView.visibility = View.GONE
        imageNoResultsError.visibility = View.VISIBLE
        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE
        searchHistoryTitle.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE


    }

    private fun showNetworkErrorSearchActivity(message: String) {
        trackListRecyclerView.visibility = View.GONE
        imageNoResultsError.visibility = View.GONE
        imageNetworkError.visibility = View.VISIBLE
        refreshButton.visibility = View.VISIBLE
        searchHistoryTitle.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE

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
