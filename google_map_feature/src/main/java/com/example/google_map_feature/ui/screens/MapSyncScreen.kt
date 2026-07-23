package com.example.google_map_feature.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.google_map_feature.ui.viewModels.MapViewModel
import com.example.navigation.domain.NavigateToDownloadScreen
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapSyncScreen(
    viewModel: MapViewModel = viewModel()
) {
    val events = viewModel.events
    val selectedId by viewModel.selectedId.collectAsState()
    val listState = rememberLazyListState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(54.68, 25.27), 12f)
    }

    LaunchedEffect(selectedId) {
        selectedId?.let { id ->
            val index = events.indexOfFirst { it.id == id }
            if (index != -1) listState.animateScrollToItem(index)
        }
    }

    // Scaffold ДОЛЖЕН быть корневым элементом
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetPeekHeight = 200.dp,
        sheetContent = {
            // Контент шторки
            Box(Modifier.fillMaxWidth().height(300.dp)) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
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
        }
    ) { paddingValues ->
        // Основной контент экрана (Карта + Кнопка поверх)
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // Учитываем отступы Scaffold
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

            // Кнопка навигации поверх карты
            Button(
                onClick = {
                    // Вызываем навигацию через роутер
                    viewModel.navigateTo(NavigateToDownloadScreen)
                },
                modifier = Modifier
                    .align(Alignment.TopCenter) // Или BottomCenter, но там шторка
                    .padding(top = 16.dp)
            ) {
                Text("Go to Download Screen")
            }
        }
    }
}