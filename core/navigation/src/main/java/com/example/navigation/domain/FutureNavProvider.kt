package com.example.navigation.domain

import androidx.fragment.app.Fragment

interface FutureNavProvider {
    fun createFragment(navAction: NavAction): Fragment?
}