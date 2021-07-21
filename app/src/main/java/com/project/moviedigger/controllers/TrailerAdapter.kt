package com.project.moviedigger.controllers

import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.project.moviedigger.R
import com.project.moviedigger.models.YoutubeVideo


class TrailerAdapter(recyclerData : ArrayList<YoutubeVideo>)  : RecyclerView.Adapter<RecyclerView.ViewHolder>(), LifecycleOwner {

    private var items: ArrayList<YoutubeVideo> = recyclerData
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VideoViewHolder -> {
                holder.bind(items[position], lifecycleRegistry)
            }
        }
    }

    class VideoViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(video: YoutubeVideo, lifecycleRegistry: LifecycleRegistry) {
            //Change player properties
            val youTubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.trailer_video)
            lifecycleRegistry.addObserver(youTubePlayerView)
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val titleView: TextView = itemView.findViewById(R.id.trailer_title)
                    titleView.text = video.title
                    val releaseView: TextView = itemView.findViewById(R.id.trailer_date)
                    releaseView.text = video.publishedAt.substringBefore("T")
                    youTubePlayer.cueVideo(video.videoId, 0f)
                }
            })
        }
    }
}
