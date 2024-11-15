package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(private var tracks: List<Track>, private val onItemClickListener: (Track) -> Unit) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    fun setTracks(updatedTracks: List<Track>) {
        this.tracks = updatedTracks
        notifyDataSetChanged()  // Перезагружаем список
    }



    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artworkImage: ImageView = itemView.findViewById(R.id.artworkImage)
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName

            val durationInMillis = track.trackTimeMillis ?: 0
            val minutes = (durationInMillis / 1000) / 60
            val seconds = (durationInMillis / 1000) % 60
            trackTimeMillis.text = String.format("%d:%02d", minutes, seconds)

            // Проверка и загрузка изображения с использованием Glide
            val imageUrl = track.artworkUrl100
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder) // Плейсхолдер, если изображение еще не загружено
                .error(R.drawable.placeholder) // Плейсхолдер, если изображение не доступно
                .into(artworkImage)

            // Обработчик кликов по элементу
            itemView.setOnClickListener {
                onItemClickListener(track) // Вызываем функцию из активности или фрагмента
            }
        }
    }

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }
}
