package com.zego.chathouse.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.zego.chathouse.R
import com.zego.chathouse.constants.ZegoConstant
import com.zego.chathouse.ui.base.BaseActivity
import im.zego.zegoexpress.ZegoExpressEngine
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private var roleType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        roleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.guestRadioBtn -> {
                    roleType = 0
                }
                R.id.audienceRadioBtn -> {
                    roleType = 1
                }
            }
        }

        enterRoomBtn.setOnClickListener {
            val roomId = roomIdEditText.text.toString()
            if (TextUtils.isEmpty(roomId)) {
                toast(R.string.hint_please_input_room_num)
                return@setOnClickListener
            }
            val intent = Intent(this@MainActivity, ChatRoomActivity::class.java)
            intent.putExtra(ZegoConstant.ROOM_ID, roomId)
            intent.putExtra(ZegoConstant.ROLE_TYPE, roleType)
            startActivity(intent)
        }
    }

    override fun finish() {
        super.finish()

        ZegoExpressEngine.destroyEngine(null)
    }
}