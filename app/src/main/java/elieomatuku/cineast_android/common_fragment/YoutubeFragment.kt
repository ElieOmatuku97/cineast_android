package elieomatuku.cineast_android.common_fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView
import elieomatuku.cineast_android.R
import kotlinx.android.synthetic.main.widget_youtube.view.*


class YoutubeFragment: Fragment() {
    companion object {
        val LOG_TAG = YoutubeFragment::class.java.simpleName

        fun newInstance(youtubeUrl: String): YoutubeFragment{
            val fragment =  YoutubeFragment()
            val args = Bundle()
            args.putString("youtube_url", youtubeUrl )
            fragment.arguments = args
            return fragment
        }
    }

    private var player: YouTubePlayer? = null

    private var lastVideoId: String? = null
    private var lastDuration: Float = 0f
    private var lastPosition: Float = 0f
    private var playing = false

    private var initComplete = false

    private var listener = object: AbstractYouTubePlayerListener() {

        override fun onReady() {
            super.onReady()
            Log.d(LOG_TAG, "onReady")
            updateVideo()
        }


        override fun onVideoDuration(duration: Float) {
            super.onVideoDuration(duration)
            if (!initComplete) {
                Log.d(LOG_TAG, "onVideoDuration $duration")
                lastDuration = duration
                initComplete = true
            }
        }

        override fun onVideoLoadedFraction(fraction: Float) {
            super.onVideoLoadedFraction(fraction)
            if (initComplete) {
                lastPosition = fraction*lastDuration
            }
        }

        override fun onStateChange(state: Int) {
            super.onStateChange(state)

            playing = when (state) {
                PlayerConstants.PlayerState.PLAYING -> true
                PlayerConstants.PlayerState.PAUSED -> false
                else -> playing
            }

            Log.d(LOG_TAG, "onStateChange: state = $state, playing = $playing")
        }
    }

    private fun reset() {
        Log.d(LOG_TAG, "reset")
        playing = false
        lastPosition = 0f
        lastDuration = 0f
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.widget_youtube, container, false)

        val exitIconView: ImageView by lazy {
            view.close_icon
        }

        exitIconView.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        val youTubePlayerView: YouTubePlayerView by lazy {
            val ytview = view.youtube_widget
            ytview.enterFullScreen()
            ytview.playerUIController.showFullscreenButton(true)
            ytview.playerUIController.showPlayPauseButton(true)
            lifecycle.addObserver(ytview)
            ytview
        }

        initComplete = false

        val videoId = arguments?.getString("youtube_url")
        if (videoId != lastVideoId) {
            reset()
            lastVideoId = videoId
            Log.d(LOG_TAG, "updateList: lastVideoId = $lastVideoId")
        }


        if (player != null) {
            updateVideo()

        } else {
            youTubePlayerView.initialize({ initializedYouTubePlayer: YouTubePlayer ->
                player = initializedYouTubePlayer
                initializedYouTubePlayer.addListener(listener)

            }, true)
        }
        return  view
    }

    private fun updateVideo() {
        lastVideoId?.let {
            if (playing) {
                player?.loadVideo(it, lastPosition)

            } else {
                player?.cueVideo(it, lastPosition)
            }
        }
    }
}