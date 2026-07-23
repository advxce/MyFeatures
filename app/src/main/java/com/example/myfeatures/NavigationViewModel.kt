package com.example.myfeatures

import androidx.lifecycle.ViewModel
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.NavigateToGoogleMapScreen
import com.example.navigation.domain.Router
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.Route

class NavigationViewModel: ViewModel(), Router {

    private val _currentAction = MutableSharedFlow<NavAction>(
        replay = 1,
        extraBufferCapacity = 1)
    val currentAction = _currentAction.asSharedFlow()

    override fun navigateTo(navAction: NavAction) {
        _currentAction.tryEmit(navAction)
    }

    override fun back() {

    }

}