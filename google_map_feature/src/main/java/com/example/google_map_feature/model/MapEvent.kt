package com.example.google_map_feature.model

import com.google.android.gms.maps.model.LatLng


data class MapEvent(val id: String, val position: LatLng, val title: String)