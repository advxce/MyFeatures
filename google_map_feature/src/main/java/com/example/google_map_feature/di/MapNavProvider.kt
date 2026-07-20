package com.example.google_map_feature.di

import androidx.fragment.app.Fragment
import com.example.google_map_feature.ui.screens.MapFragment
import com.example.navigation.domain.FutureNavProvider
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.NavigateToGoogleMapScreen

class MapNavProvider : FutureNavProvider {
    override fun createFragment(navAction: NavAction): Fragment? {
        return if (navAction is NavigateToGoogleMapScreen)
            MapFragment()
        else null

    }

}