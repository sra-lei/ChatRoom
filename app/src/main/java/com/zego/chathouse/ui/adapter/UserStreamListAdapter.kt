package com.zego.chathouse.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zego.chathouse.R
import com.zego.chathouse.ui.adapter.UserStreamListAdapter.UserStreamViewHolder
import im.zego.zegoexpress.entity.ZegoUser
import java.util.*

/**
 * Created by zego on 2018/2/6.
 */
class UserStreamListAdapter :
    BaseQuickAdapter<ZegoUser, UserStreamViewHolder>(R.layout.item_user_stream, ArrayList()) {
    private val highlightColor = Color.parseColor("#1266FF")

    override fun convert(holder: UserStreamViewHolder, item: ZegoUser) {
        holder.userName.text = item.userName
    }

    class UserStreamViewHolder internal constructor(itemView: View) : BaseViewHolder(itemView) {
        var userIcon: ImageView = itemView.findViewById(R.id.userIconImg)
        var userName: TextView = itemView.findViewById(R.id.userNameTextView)
    }
}