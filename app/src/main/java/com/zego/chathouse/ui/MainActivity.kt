package com.zego.chathouse.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.zego.chathouse.R
import com.zego.chathouse.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterRoomBtn.setOnClickListener {
            val roomId = roomIdEditText.text
            if (TextUtils.isEmpty(roomId)) {
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, ChatRoomActivity::class.java)
            startActivity(intent)
        }
    }
}