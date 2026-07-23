package com.example.download_manager_feature

import androidx.fragment.app.Fragment
import com.example.navigation.domain.FutureNavProvider
import com.example.navigation.domain.NavAction
import com.example.navigation.domain.NavigateToDownloadScreen

class DownloadNavProvider: FutureNavProvider {
    override fun createFragment(navAction: NavAction): Fragment? {
        return if(navAction is NavigateToDownloadScreen){
            DownloadManagerFragment()
        } else{
            null
        }
    }

}