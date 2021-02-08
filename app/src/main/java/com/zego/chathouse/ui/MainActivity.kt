package com.zego.chathouse.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.zego.chathouse.R
import com.zego.chathouse.constants.ZegoConstant
import com.zego.chathouse.ui.base.BaseActivity
import im.zego.zegoexpress.ZegoExpressEngine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enterRoomBtn.setOnClickListener {
            val roomId = roomIdEditText.text.toString()
            if (TextUtils.isEmpty(roomId)) {
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, ChatRoomActivity::class.java)
            intent.putExtra(ZegoConstant.ROOM_ID, roomId)
            startActivity(intent)
        }
    }

    override fun finish() {
        super.finish()

        ZegoExpressEngine.getEngine().setEventHandler(null)
        ZegoExpressEngine.destroyEngine(null)
    }
}