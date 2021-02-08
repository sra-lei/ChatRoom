package com.zego.chathouse.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zego.chathouse.R
import com.zego.chathouse.ui.adapter.UserStreamListAdapter.UserStreamViewHolder
import com.zego.chathouse.ui.vo.ChatStreamInfo
import im.zego.zegoexpress.entity.ZegoUser
import java.util.*

/**
 * Created by zego on 2018/2/6.
 */
class UserStreamListAdapter : BaseQuickAdapter<ChatStreamInfo, UserStreamViewHolder>(R.layout.item_user_stream, mutableListOf()) {
    private val highlightColor = Color.parseColor("#1266FF")

    override fun convert(holder: UserStreamViewHolder, item: ChatStreamInfo) {
        holder.userName.text = item.user.userName
        holder.soundLevelTextView.text = item.getSoundLevel().toString()
    }

    fun addData(zegoUsers: List<ZegoUser>) {
        for (zegoUser in zegoUsers) {
            this.addData(ChatStreamInfo(zegoUser))
        }
    }

    fun remove(zegoUser: ZegoUser) {
        var removed: ChatStreamInfo? = null
        for (chatStreamInfo in data) {
            if (chatStreamInfo.user.userID == zegoUser.userID) {
                removed = chatStreamInfo
                break
            }
        }
        removed?.let {
            this.remove(it)
        }
    }

    fun update(soundLevels: HashMap<String, Float>) {
        for (chatStreamInfo in data) {
            soundLevels[chatStreamInfo.streamID]?.let {
                chatStreamInfo.setSoundLevel(it)
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