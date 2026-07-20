package com.example.google_map_feature.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.google_map_feature.ui.viewModels.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSyncScreen(viewModel: MapViewModel = viewModel()) {
    val events = viewModel.events
    val selectedId by viewModel.selectedId.collectAsState()
    val listState = rememberLazyListState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(54.68, 25.27), 12f)
    }

    // Синхронизация: прокрутка списка при выборе маркера
    LaunchedEffect(selectedId) {
        selectedId?.let { id ->
            val index = events.indexOfFirst { it.id == id }
            if (index != -1) listState.animateScrollToItem(index)
        }
    }

    Box(modifier = Modifier.fillMaxSize()){


        // Шторка с обычным LazyColumn
        BottomSheetScaffold(
            sheetPeekHeight = 200.dp,
            sheetContent = {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.height(300.dp).padding(16.dp)
                ) {
                    items(events) { event ->
                        val isSelected = event.id == selectedId
                        Text(
                            text = event.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(if (isSelected) Color.LightGray else Color.Transparent)
                                .clickable { viewModel.selectEvent(event.id) }
                        )
                    }
                }
            }
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                events.forEach { event ->
                    Marker(
                        state = MarkerState(position = event.position),
                        title = event.title,
                        onClick = {
                            viewModel.selectEvent(event.id)
                            true
                        }
                    )
                }
            }
        }
    }
}