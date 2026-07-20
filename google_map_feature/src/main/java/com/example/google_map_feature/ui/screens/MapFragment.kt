package com.example.google_map_feature.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.google_map_feature.R
import com.example.google_map_feature.ui.viewModels.MapViewModel
import com.example.navigation.domain.Router

class MapFragment : Fragment() {

    private var router: Router? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        router = requireActivity() as? Router

        val view = inflater.inflate(R.layout.fragment_map, container, false)


        view.findViewById<ComposeView>(R.id.mapScreen).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val router = requireActivity() as Router
                val viewModel: MapViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return MapViewModel(router) as T
                        }
                    }
                )
                MapSyncScreen(viewModel = viewModel)
            }
        }
        return view
    }

    override fun onDestroyView() {
        router = null
        super.onDestroyView()
    }

}