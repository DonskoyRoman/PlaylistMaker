package com.example.playlistmaker

data class Track(
    val trackId: Long,
    val collectionName : String,
    val releaseDate : String,
    val primaryGenreName :String,
    val country : String,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    fun getReleaseYear() = releaseDate.substring(0, releaseDate.indexOf("-"))
}

