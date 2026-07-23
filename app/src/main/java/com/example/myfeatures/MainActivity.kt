package com.example.myfeatures

import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.download_manager_feature.DownloadManagerFragment
import com.example.download_manager_feature.DownloadNavProvider
import com.example.google_map_feature.di.MapNavProvider
import com.example.google_map_feature.ui.screens.MapFragment
import com.example.google_map_feature.ui.screens.MapSyncScreen
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.NavigateToGoogleMapScreen
import com.example.navigation.domain.Router
import kotlinx.coroutines.launch
import okhttp3.Route

class MainActivity : AppCompatActivity(), Router {


    private val navViewModel: NavigationViewModel by viewModels()

    private val navProviders = listOf(
        MapNavProvider(),
        DownloadNavProvider()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeNavAction()

        if (savedInstanceState == null) {
            navViewModel.navigateTo(NavigateToGoogleMapScreen)
        }
    }


    fun observeNavAction() {

        lifecycleScope.launch {
            navViewModel.currentAction.collect { action ->
                val fragment = navProviders.firstNotNullOfOrNull { it.createFragment(action) }
                if (fragment != null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

    }

    override fun navigateTo(navAction: NavAction) {
        navViewModel.navigateTo(navAction)
    }

    override fun back() {
        onBackPressedDispatcher.onBackPressed()
    }

}