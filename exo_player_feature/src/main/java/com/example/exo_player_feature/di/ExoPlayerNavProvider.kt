package com.example.exo_player_feature.di

import androidx.fragment.app.Fragment
import com.example.exo_player_feature.ui.ExoPlayerFragment
import com.example.navigation.domain.FutureNavProvider
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.NavigateToExoPlayerScreen

class ExoPlayerNavProvider : FutureNavProvider {
    override fun createFragment(navAction: NavAction): Fragment? {
        return if (navAction is NavigateToExoPlayerScreen) {
            ExoPlayerFragment()
        } else null
    }
}