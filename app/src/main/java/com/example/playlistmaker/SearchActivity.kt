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
    private lateinit var imageNoResultsError: LinearLayout
    private lateinit var imageNetworkError: LinearLayout
    private lateinit var clearIcon: ImageView
    private lateinit var refreshButton: Button
    private val tracks = ArrayList<Track>()
//    private lateinit var searchViewModel: SearchViewModel
//    private lateinit var adapter: TrackAdapter

    private val adapter = TrackAdapter(tracks)
    private val api: ITunesApi = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        queryInput = findViewById(R.id.inputEditText)
        trackListRecyclerView = findViewById(R.id.trackListRecyclerView)
        imageNoResultsError = findViewById(R.id.imageNoResultsErrorXML)
        imageNetworkError = findViewById(R.id.imageNetworkErrorXML)
        clearIcon = findViewById(R.id.clearIcon)
        refreshButton = findViewById(R.id.refreshButton)

        clearIcon.setOnClickListener {
            queryInput.text.clear()
            clearIcon.visibility = View.GONE
        }
        

        val trackHistoryManager = TrackHistoryManager(this)

        adapter.setOnItemClickListener(object : TrackAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                trackHistoryManager.saveTrackToHistory(track)
                Toast.makeText(this@SearchActivity, "$track trackAddedToHistory", Toast.LENGTH_SHORT).show()
            }
        })

        trackListRecyclerView.layoutManager = LinearLayoutManager(this)
        trackListRecyclerView.adapter = adapter
//
        queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (queryInput.text.isNotEmpty()) {
                    searchTracks(queryInput.text.toString())

                } else {
                    showTrackHistory()
                }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(queryInput.windowToken, 0)
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
            imageNoResultsError.visibility = View.VISIBLE
            trackListRecyclerView.visibility = View.GONE
        } else {
            imageNoResultsError.visibility = View.GONE
            trackListRecyclerView.visibility = View.VISIBLE
            adapter.setTracks(trackHistory)
        }
    }

    private fun searchTracks(query: String) {
        Log.d("SearchActivity", "Searching for query: $query")
        tracks.clear()
        adapter.notifyDataSetChanged()

        api.searchTracks(query).enqueue(object : Callback<TrackSearchResponse> {
            override fun onResponse(
                call: Call<TrackSearchResponse>,
                response: Response<TrackSearchResponse>
            ) {
                Log.d("SearchActivity", "Received response: $response")
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

                    400 -> showNetworkErrorSearchActivity("Неверный запрос")
                    403 -> showNetworkErrorSearchActivity("Доступ запрещён")
                    500 -> showNetworkErrorSearchActivity("Ошибка на сервере")
                    else -> showNetworkErrorSearchActivity("Неизвестная ошибка")
                }
            }

            override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {
                Log.e("SearchActivity", "Error searching for tracks: $t")
                showNetworkErrorSearchActivity("Ошибка сети")
            }
        })
    }

    private fun showTracks() {
        trackListRecyclerView.visibility = View.VISIBLE
        imageNoResultsError.visibility = View.GONE
        imageNetworkError.visibility = View.GONE
        refreshButton.visibility = View.GONE

        Toast.makeText(this, "showTracks", Toast.LENGTH_SHORT).show()
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
