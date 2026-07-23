package com.example.exo_player_feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.exo_player_feature.databinding.FragmentExoPlayerBinding
import com.example.navigation.domain.NavigateToGoogleMapScreen
import com.example.navigation.domain.Router

class ExoPlayerFragment: Fragment() {

    private var router: Router? = null
    private var _binding: FragmentExoPlayerBinding? = null
    private val binding get() = _binding!!

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = (requireActivity() as? Router)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExoPlayerBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBtn()
    }

    private fun initBtn(){
        binding.btnToDownloadManager.setOnClickListener {
            router?.navigateTo(NavigateToGoogleMapScreen)
        }
    }

    override fun onStart() {
        super.onStart()
        initPlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    fun releasePlayer(){
        player?.release()
        player = null
    }

    override fun onDestroyView() {
        _binding = null
        router = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initPlayer(){
        player = ExoPlayer.Builder(requireContext()).build().also { exoPlayer ->
            binding.playerView.player = exoPlayer

            val mediaItem = MediaItem.fromUri("")
            exoPlayer.setMediaItem(mediaItem)

            exoPlayer.prepare()
        }
    }

}