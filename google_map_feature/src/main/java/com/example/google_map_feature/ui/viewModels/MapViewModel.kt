package com.example.google_map_feature.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.google_map_feature.model.MapEvent
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.Router
import com.example.remoteConfig.RemoteConfigProvider
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel(
    private val router: Router,
    private val remoteConfigProvider: RemoteConfigProvider
) : ViewModel() {
    val events = List(10) { i ->
        MapEvent(i.toString(), LatLng(54.68 + (i * 0.01), 25.27 + (i * 0.01)), "Событие $i")
    }

    val isExoPlayerFeatureEnabled = MutableStateFlow<Boolean>(
        remoteConfigProvider.appConfig.isFeatureEnabled("exo_player_feature")
    ).asStateFlow()

    private val _selectedId = MutableStateFlow<String?>(null)
    val selectedId = _selectedId.asStateFlow()

    fun navigateTo(navAction: NavAction) { router.navigateTo(navAction)}


    fun selectEvent(id: String?) { _selectedId.value = id }
}