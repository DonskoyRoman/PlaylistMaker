package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners


class AudioplayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)


        val trackName = intent.getStringExtra("track_name") ?: "Unknown"
        val artistName = intent.getStringExtra("artist_name") ?: "Unknown"
        val collectionName = intent.getStringExtra("collection_name") ?: "Unknown"
        val releaseYear = intent.getStringExtra("release_year") ?: "Unknown"
        val genre = intent.getStringExtra("genre") ?: "Unknown"
        val country = intent.getStringExtra("country") ?: "Unknown"
        val duration = intent.getLongExtra("duration", 0L)
        val artworkUrl = intent.getStringExtra("artwork_url")

        val backButton = findViewById<ImageView>(R.id.back_button_player)
        backButton.setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.trackTextView).text = trackName
        findViewById<TextView>(R.id.artistTextView).text = artistName
        findViewById<TextView>(R.id.albumValueTextView).text = collectionName
        findViewById<TextView>(R.id.yearValueTextView).text = releaseYear
        findViewById<TextView>(R.id.genreValueTextView).text = genre
        findViewById<TextView>(R.id.countryValueTextView).text = country

        val minutes = duration / 60000
        val seconds = (duration % 60000) / 1000
        findViewById<TextView>(R.id.durationValueTextView).text = String.format("%d:%02d", minutes, seconds)

        val cornerRadius =
            this.resources.getDimensionPixelSize(R.dimen.medium_padding_8dp)

        artworkUrl?.let {
            Glide.with(this)
                .load(it)
                .transform(RoundedCorners(cornerRadius))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(findViewById(R.id.cover_image))
        }
    }
}
