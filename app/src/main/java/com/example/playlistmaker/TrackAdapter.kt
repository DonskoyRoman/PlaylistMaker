package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackAdapter(private var tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks
        notifyDataSetChanged()
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artworkImage: ImageView = itemView.findViewById(R.id.artworkImage)
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val artistName: TextView = itemView.findViewById(R.id.artistName)
        private val trackTimeMillis: TextView = itemView.findViewById(R.id.trackTime)
        init {
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(tracks[adapterPosition])
            }
        }

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName

            val durationInMillis = track.trackTimeMillis ?: 0
            val minutes = (durationInMillis / 1000) / 60
            val seconds = (durationInMillis / 1000) % 60
            trackTimeMillis.text = String.format("%d:%02d", minutes, seconds)


            val cornerRadius =
                itemView.context.resources.getDimensionPixelSize(R.dimen.very_small_corner_radius)

            //val imageUrl = track.artworkUrl100
            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .transform(RoundedCorners(cornerRadius))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(artworkImage)
        }
    }
}




