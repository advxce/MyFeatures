package com.example.download_manager_feature

import androidx.recyclerview.widget.DiffUtil

class DownloadDiffUtil : DiffUtil.ItemCallback<DownloadItem>() {
    override fun areItemsTheSame(
        old: DownloadItem,
        new: DownloadItem
    ): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(
        old: DownloadItem,
        new: DownloadItem
    ): Boolean {
        return old == new
    }

    override fun getChangePayload(oldItem: DownloadItem, newItem: DownloadItem): Any? {
        val payloadSet = mutableSetOf<String>()
        if (oldItem.status != newItem.status) {
            payloadSet.add(PAYLOAD_STATUS)
        }
        if (oldItem.progress != newItem.progress) {
            payloadSet.add(PAYLOAD_PROGRESS)
        }
        return if (payloadSet.isEmpty())
            super.getChangePayload(oldItem, newItem)
        else payloadSet

    }

    companion object {
        const val PAYLOAD_STATUS = "PAYLOAD_STATUS"
        const val PAYLOAD_PROGRESS = "PAYLOAD_PROGRESS"
    }


}