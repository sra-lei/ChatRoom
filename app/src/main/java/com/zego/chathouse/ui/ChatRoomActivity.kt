package com.zego.chathouse.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.zego.chathouse.R
import com.zego.chathouse.constants.ZegoConstant
import com.zego.chathouse.ui.adapter.UserStreamListAdapter
import com.zego.chathouse.ui.base.BaseActivity
import com.zego.chathouse.utils.ZegoPreferenceUtil
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.callback.IZegoEventHandler
import im.zego.zegoexpress.constants.ZegoAudioConfigPreset
import im.zego.zegoexpress.constants.ZegoPlayerState
import im.zego.zegoexpress.constants.ZegoRoomState
import im.zego.zegoexpress.constants.ZegoUpdateType
import im.zego.zegoexpress.entity.ZegoAudioConfig
import im.zego.zegoexpress.entity.ZegoRoomConfig
import im.zego.zegoexpress.entity.ZegoStream
import im.zego.zegoexpress.entity.ZegoUser
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

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

    // current room Id
    private var mRoomId: String? = ""

    //  role type
    private var mRoleType: Int = 0

    // self
    private val mSelfUser = createUser()

    // self publish stream Id
    private var mPublishStreamId: String = ""

    // cur users in room
    private val mRoomUsers = ArrayList<ZegoUser>()

    // cur streams in room
    private val mRoomStreams = ArrayList<ZegoStream>()

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

        micOnBtn.setOnClickListener {
            if (mEngine.isMicrophoneMuted) {
                mEngine.muteMicrophone(false)
                it.isEnabled = false
                micOffBtn.isEnabled = true
            }
        }

        micOffBtn.setOnClickListener {
            mEngine.muteMicrophone(true)
            it.isEnabled = false
            micOnBtn.isEnabled = true
        }

        exitRoomBtn.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        mRoomId = intent.getStringExtra(ZegoConstant.ROOM_ID)
        val config = ZegoRoomConfig()
        config.isUserStatusNotify = true
        mEngine.loginRoom(mRoomId, mSelfUser, config)
        // mute microphone
        mEngine.muteMicrophone(true)
        // monitor for sound level
        mEngine.startSoundLevelMonitor(300)

        mRoleType = intent.getIntExtra(ZegoConstant.ROLE_TYPE, 0)
        if (mRoleType == 0) {
            startPublish()
            toolBoxLayout.visibility = View.VISIBLE
        } else {
            toolBoxLayout.visibility = View.GONE
        }
    }

    /**
     * start publish
     */
    private fun startPublish() {
        // audio config
        val audioConfig = ZegoAudioConfig(ZegoAudioConfigPreset.BASIC_QUALITY)
        mEngine.audioConfig = audioConfig
        // start publish
        mPublishStreamId = createStreamId()
        mEngine.startPublishingStream(mPublishStreamId)
    }

    override fun finish() {
        mEngine.stopSoundLevelMonitor()
        mEngine.stopPublishingStream()
        for (stream in mRoomStreams) {
            mEngine.stopPlayingStream(stream.streamID)
        }
        mEngine.setEventHandler(null)
        mEngine.logoutRoom(mRoomId)
        super.finish()
    }

    private inner class ChatRoomEventHandler : IZegoEventHandler() {
        override fun onRoomStateUpdate(roomID: String?, state: ZegoRoomState?, errorCode: Int, extendedData: JSONObject?) {
            super.onRoomStateUpdate(roomID, state, errorCode, extendedData)
            when {
                ZegoRoomState.CONNECTING == state -> {
                    AnkoLogger(TAG).debug { "login complete." }
                }
                ZegoRoomState.CONNECTED == state -> {
                    AnkoLogger(TAG).debug { "login complete." }
                }
                ZegoRoomState.DISCONNECTED == state -> {
                    toast("disconnect from room.")
                }
            }
        }

        override fun onRoomUserUpdate(roomID: String, updateType: ZegoUpdateType, userList: ArrayList<ZegoUser>) {
            super.onRoomUserUpdate(roomID, updateType, userList)
            when {
                ZegoUpdateType.ADD == updateType -> {
                    mRoomUsers.addAll(userList)
                }
                ZegoUpdateType.DELETE == updateType -> {
                    mRoomUsers.removeAll(userList)
                }
            }
            runOnUiThread {
                userCountText.text = getString(R.string.online_user_count, mRoomUsers.size + 1)
            }
        }

        override fun onRoomOnlineUserCountUpdate(roomID: String?, count: Int) {
            super.onRoomOnlineUserCountUpdate(roomID, count)
            runOnUiThread {
                userCountText.text = getString(R.string.online_user_count, count)
            }
        }

        override fun onRoomStreamUpdate(roomID: String?, updateType: ZegoUpdateType, streamList: ArrayList<ZegoStream>, extendedData: JSONObject?) {
            super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
            when {
                ZegoUpdateType.ADD == updateType -> {
                    for (stream in streamList) {
                        mEngine.startPlayingStream(stream.streamID)
                    }
                    mAdapter.addZegoStreams(streamList)
                    mRoomStreams.addAll(streamList)
                }
                ZegoUpdateType.DELETE == updateType -> {
                    for (stream in streamList) {
                        mEngine.stopPlayingStream(stream.streamID)
                    }
                    mAdapter.removeZegoStreams(streamList)
                    mRoomStreams.removeAll(streamList)
                }
            }
        }

        override fun onPlayerStateUpdate(streamID: String?, state: ZegoPlayerState?, errorCode: Int, extendedData: JSONObject?) {
            super.onPlayerStateUpdate(streamID, state, errorCode, extendedData)
            if (0 != errorCode) {
                mEngine.startPlayingStream(streamID)
            }
        }

        override fun onCapturedSoundLevelUpdate(soundLevel: Float) {
            super.onCapturedSoundLevelUpdate(soundLevel)
            runOnUiThread {
                captureSoundLevelTxt.text = soundLevel.toString()
            }
        }

        override fun onRemoteSoundLevelUpdate(soundLevels: HashMap<String, Float>) {
            super.onRemoteSoundLevelUpdate(soundLevels)
            mAdapter.update(soundLevels)
        }
    }

    private fun createUser(): ZegoUser {
        val userId = ZegoPreferenceUtil.instance.getUserID()
        val userName = ZegoPreferenceUtil.instance.getUserName()
        return ZegoUser(userId, userName)
    }

    private fun createStreamId(): String {
        return mRoomId + "-" + UUID.randomUUID()
    }
}