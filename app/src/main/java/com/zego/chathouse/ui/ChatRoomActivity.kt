package com.zego.chathouse.ui

import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.zego.chathouse.R
import com.zego.chathouse.constants.ZegoConstant
import com.zego.chathouse.ui.adapter.UserStreamListAdapter
import com.zego.chathouse.ui.base.BaseActivity
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoRoomState
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.entity.ZegoStream
import im.zego.zegoexpress.entity.ZegoUser
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.*

/**
 * ChatRoom
 *
 * @date 2/8/21 4:35 PM
 * @author luke
 * description:
 */
class ChatRoomActivity : BaseActivity() {
    companion object {
        private const val TAG = "ChatRoomActivity"
    }

    private var mEngine: ZegoExpressEngine = ZegoExpressEngine.getEngine()
    private var mEventHandler: ChatRoomEventHandler

    private val mAdapter = UserStreamListAdapter()

    private var mRoomId: String? = ""

    init {
        mEventHandler = ChatRoomEventHandler()
        mEngine.setEventHandler(mEventHandler)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        // init views and set listeners to them
        initViews()
        // init zego express sdk.
        initData()
    }

    private fun initViews() {
        userRecyclerView.layoutManager = GridLayoutManager(this, 3)
        userRecyclerView.itemAnimator = DefaultItemAnimator()
        userRecyclerView.adapter = mAdapter

        refreshLayout.setOnRefreshListener {

        }

        micOnBtn.setOnClickListener {
            if (mEngine.isMicrophoneMuted) {
                mEngine.muteMicrophone(false)
            }
        }

        micOffBtn.setOnClickListener {
            mEngine.muteMicrophone(true)
        }

        exitRoomBtn.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        mRoomId = intent.getStringExtra(ZegoConstant.ROOM_ID)
        mEngine.loginRoom(mRoomId, createUser())
    }

    override fun finish() {
        mEngine.setEventHandler(null)
        mEngine.logoutRoom(mRoomId)
        super.finish()
    }

    private inner class ChatRoomEventHandler : IZegoEventHandler() {
        override fun onRoomStateUpdate(
            roomID: String?, state: ZegoRoomState?, errorCode: Int, extendedData: JSONObject?
        ) {
            super.onRoomStateUpdate(roomID, state, errorCode, extendedData)
            when {
                ZegoRoomState.CONNECTING == state -> {
                    AnkoLogger(TAG).debug { "login complete." }
                }
                ZegoRoomState.CONNECTED == state -> {
                    AnkoLogger(TAG).debug { "login complete." }
                    for (i in 0..10) {
                        mAdapter.addData(createUser())
                    }
                }
                ZegoRoomState.DISCONNECTED == state -> {
                    toast("disconnect from room.")
                }
            }
        }

        override fun onRoomUserUpdate(
            roomID: String?, updateType: ZegoUpdateType?, userList: ArrayList<ZegoUser>?
        ) {
            super.onRoomUserUpdate(roomID, updateType, userList)
        }

        override fun onRoomStreamUpdate(
            roomID: String?,
            updateType: ZegoUpdateType?,
            streamList: ArrayList<ZegoStream>?,
            extendedData: JSONObject?
        ) {
            super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
        }
    }

    private fun createUser(): ZegoUser {
        val userId = "User-${System.currentTimeMillis()}"
        val userName = "UserName-${System.currentTimeMillis()}"
        return ZegoUser(userId, userName)
    }
}