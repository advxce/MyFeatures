package com.example.google_map_feature.ui.viewModels

import androidx.lifecycle.ViewModel
import com.example.google_map_feature.model.MapEvent
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MapViewModel : ViewModel() {
    val events = List(10) { i ->
        MapEvent(i.toString(), LatLng(54.68 + (i * 0.01), 25.27 + (i * 0.01)), "Событие $i")
    }

    private val _selectedId = MutableStateFlow<String?>(null)
    val selectedId = _selectedId.asStateFlow()

    fun selectEvent(id: String?) { _selectedId.value = id }
}