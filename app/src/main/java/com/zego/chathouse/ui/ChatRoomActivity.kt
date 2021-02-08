package com.zego.chathouse.ui

import android.os.Bundle
import com.zego.chathouse.R
import com.zego.chathouse.ui.base.BaseActivity

/**
 *
 * @date 2/8/21 4:35 PM
 * @author luke
 * description:
 */
class ChatRoomActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
    }
}