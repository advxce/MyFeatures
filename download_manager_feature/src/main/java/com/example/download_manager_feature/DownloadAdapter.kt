package com.example.download_manager_feature

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.download_manager_feature.databinding.DownloadItemBinding

class DownloadAdapter(
    private val onDownloadClick: (DownloadItem) -> Unit,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, DownloadDiffUtil())

    fun updateList(newList: List<DownloadItem>){
        differ.submitList(newList)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val downloadItem = DownloadItemBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup, false)
        return DownloadViewHolder(downloadItem)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as DownloadViewHolder).bindFull(differ.currentList[position])
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<Any?>
    ) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        } else{
            val combinedPayloads = payloads.firstOrNull() as? Set<String> ?: return
            (holder as DownloadViewHolder).bindPart(differ.currentList[position], combinedPayloads)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class DownloadViewHolder(private val binding: DownloadItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bindFull(item: DownloadItem){
            binding.tvFilename.text = item.fileName
            updateStatusUi(item)
            updateProgressUi(item.progress)
            binding.btnDownloadFile.setOnClickListener {
                onDownloadClick(item)
            }
        }

        fun bindPart(item: DownloadItem, payloads: Set<String>){
            if(payloads.contains(DownloadDiffUtil.PAYLOAD_PROGRESS)){
                updateProgressUi(item.progress)
            }
            if(payloads.contains(DownloadDiffUtil.PAYLOAD_STATUS)){
                updateStatusUi(item)
            }

        }


        private fun updateStatusUi(downloadItem: DownloadItem) = with(binding){
            when(downloadItem.status){
                DownloadStatus.IDLE -> {
                    updateStatus(
                        isBtnVisible = true,
                        isProgressVisible = false,
                        isTextProgressVisible = false
                    )

                }
                DownloadStatus.DOWNLOADING -> {
                    updateStatus(
                        isBtnVisible = false,
                        isProgressVisible = true,
                        isTextProgressVisible = true
                    )
                    binding.tvStatus.text = "Downloading"
                }
                DownloadStatus.PAUSED -> {
                    updateStatus(
                        isBtnVisible = false,
                        isProgressVisible = true,
                        isTextProgressVisible = true
                    )
                }
                DownloadStatus.COMPLETED -> {
                    btnDownloadFile.text = "Completed"
                    binding.tvStatus.text = "Completed"
                    btnDownloadFile.isEnabled = false
                    updateStatus(
                        isBtnVisible = true,
                        isProgressVisible = false,
                        isTextProgressVisible = false
                    )
                }
            }
        }

        private fun updateStatus(
            isBtnVisible:Boolean,
            isProgressVisible:Boolean,
            isTextProgressVisible:Boolean) = with(binding){

            btnDownloadFile.visibility = if(isBtnVisible) View.VISIBLE else View.GONE
            fileProgressBar.visibility = if(isProgressVisible) View.VISIBLE else View.GONE
            tvProgress.visibility = if(isTextProgressVisible) View.VISIBLE else View.GONE
        }

        private fun updateProgressUi(progress:Int){
            binding.tvProgress.text =  "$progress%"
            binding.fileProgressBar.progress = progress
        }


    }

}