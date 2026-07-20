package com.example.myfeatures

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.download_manager_feature.DownloadManagerFragment
import com.example.google_map_feature.ui.screens.MapSyncScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main, DownloadManagerFragment())
//            .commit()

        val composeView = findViewById<ComposeView>(R.id.composeView)

        composeView.apply {
            // Важно для корректного управления жизненным циклом в Activity/Fragment
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                // Вызываем ваш Composable экран
                MapSyncScreen()
            }
        }

    }
}