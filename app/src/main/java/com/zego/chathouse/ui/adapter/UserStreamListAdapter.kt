package com.zego.chathouse.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zego.chathouse.R
import com.zego.chathouse.ui.adapter.UserStreamListAdapter.UserStreamViewHolder
import com.zego.chathouse.ui.vo.ChatStreamInfo
import com.zego.chathouse.utils.ZegoPreferenceUtil
import im.zego.zegoexpress.entity.ZegoStream

/**
 * Created by zego on 2018/2/6.
 */
class UserStreamListAdapter : BaseQuickAdapter<ChatStreamInfo, UserStreamViewHolder>(R.layout.item_user_stream, mutableListOf()) {

    override fun convert(holder: UserStreamViewHolder, item: ChatStreamInfo) {
        holder.userName.text = item.userName
        val resId = ZegoPreferenceUtil.instance.getUserIconRes(item.userName)
        holder.userIcon.setImageDrawable(ResourcesCompat.getDrawable(context.resources, resId, null))
        holder.soundLevelTextView.text = item.soundLevel.toString()
    }

    fun addZegoStreams(streamList: List<ZegoStream>) {
        for (zegoStream in streamList) {
            this.addData(ChatStreamInfo(zegoStream))
        }
    }

    private fun getItem(streamId: String): ChatStreamInfo? {
        for (streamInfo in data) {
            if (streamInfo.streamID == streamId) {
                return streamInfo
            }
        }
        return null
    }

    fun removeZegoStreams(streamList: List<ZegoStream>) {
        for (stream in streamList) {
            getItem(stream.streamID)?.let {
                this.remove(it)
            }
        }
    }

    fun update(soundLevels: HashMap<String, Float>) {
        for (chatStreamInfo in data) {
            soundLevels[chatStreamInfo.streamID]?.let {
                chatStreamInfo.soundLevel = it
            }
        }
        notifyDataSetChanged()
    }

    class UserStreamViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {
        var userIcon: ImageView = itemView.findViewById(R.id.userIconImg)
        var userName: TextView = itemView.findViewById(R.id.userNameTextView)
        var soundLevelTextView: TextView = itemView.findViewById(R.id.soundLevelTextView)
    }
}