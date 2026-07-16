package com.example.download_manager_feature

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.download_manager_feature.databinding.FragmentDownloadBinding
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class DownloadManagerFragment: Fragment() {

    lateinit var binding: FragmentDownloadBinding

    private var recAdapter: DownloadAdapter? = null


    val vmFactory: DownloadViewModelFactory by lazy {
        DownloadViewModelFactory(requireContext().applicationContext, ServiceLocator.fileDownloadApi)

    }
    private val downloadViewModel: DownloadViewModel by lazy {
        ViewModelProvider(this, vmFactory)[DownloadViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        with(binding){
            recView.layoutManager = LinearLayoutManager(requireActivity())
            recView.adapter = recAdapter
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initAdapter(){

        recAdapter = DownloadAdapter{
            downloadViewModel.downloadFile(it, requireActivity().applicationContext)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            downloadViewModel.downloadingFile.collect { list ->
                recAdapter?.updateList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}