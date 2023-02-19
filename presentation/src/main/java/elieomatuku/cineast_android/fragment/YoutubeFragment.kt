package elieomatuku.cineast_android.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayer
import elieomatuku.cineast_android.base.BaseFragment
import elieomatuku.cineast_android.databinding.WidgetYoutubeBinding

class YoutubeFragment : BaseFragment() {
    private var _binding: WidgetYoutubeBinding? = null
    private val binding get() = _binding!!

    private val args: YoutubeFragmentArgs by navArgs()

    private var player: YouTubePlayer? = null

    private var lastVideoId: String? = null
    private var lastDuration: Float = 0f
    private var lastPosition: Float = 0f
    private var playing = false

    private var initComplete = false

    private var listener = object : AbstractYouTubePlayerListener() {

        override fun onReady() {
            super.onReady()
            updateVideo()
        }

        override fun onVideoDuration(duration: Float) {
            super.onVideoDuration(duration)
            if (!initComplete) {
                lastDuration = duration
                initComplete = true
            }
        }

        override fun onVideoLoadedFraction(fraction: Float) {
            super.onVideoLoadedFraction(fraction)
            if (initComplete) {
                lastPosition = fraction * lastDuration
            }
        }

        override fun onStateChange(state: Int) {
            super.onStateChange(state)
            playing = when (state) {
                PlayerConstants.PlayerState.PLAYING -> true
                PlayerConstants.PlayerState.PAUSED -> false
                else -> playing
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WidgetYoutubeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.youtubeWidget.enterFullScreen()
        binding.youtubeWidget.playerUIController.showFullscreenButton(true)
        binding.youtubeWidget.playerUIController.showPlayPauseButton(true)
        lifecycle.addObserver(binding.youtubeWidget)

        initComplete = false

        if (args.youtubeUrl != lastVideoId) {
            reset()
            lastVideoId = args.youtubeUrl
        }

        if (player != null) {
            updateVideo()
        } else {
            binding.youtubeWidget.initialize(
                { initializedYouTubePlayer: YouTubePlayer ->
                    player = initializedYouTubePlayer
                    initializedYouTubePlayer.addListener(listener)
                },
                true
            )
        }
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

    private fun reset() {
        playing = false
        lastPosition = 0f
        lastDuration = 0f
    }
}
