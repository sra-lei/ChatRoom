# ChatHouse Development document
This document describes the process of developing ChatHouse using Zego Express Audio SDK. Some pre-processes can be completed by referring to the official Zego website, including:
1. Apply for AppId and AppSign
https://console.zegocloud.com/dashboard?lang=en
As follows: ![-w1466](media/16130119515105/16130122582982.jpg)
2. Download the SDK package
https://doc-en.zego.im/en/4114.html
3. Configure the development environment
https://doc-en.zego.im/en/4122.html
If you have any questions about the preceding steps above, you can consult ZEGO
technical support for help.
## Introduction to ChatHouse Project
### functional module
The main is divided into creating room page (MainActivity) and chat room page (ChatRoomActivity),
As shown below:
Create room page ![-w225](media/16130119515105/16130162932145.jpg)
   
Chat room page ![-w226](media/16130119515105/16130163921230.jpg)

Among them, the page of creating a room includes two functions of entering a room number and selecting a role, and the page of a chat room includes the functions of guest list, start broadcasting, stop broadcasting, and exiting the room.
### Development Instructions
#### 1、Initialize SDK
In Application, initialize the SDK, refer to the source code com.zego.chathouse.ChatHouseApplication
A brief example of initializing the SDK is as follows:
```Java
private fun initEngine() {
     // AppId and AppSign, fill in according to the actual application 
     val appID = 0000000000L
     val appSign = "ssssssss"
     val config = ZegoEngineConfig()
     config.advancedConfig["prefer_play_ultra_source"] = "1"
     config.advancedConfig["max_channels"] = "50" 
     ZegoExpressEngine.setEngineConfig(config)
     ZegoExpressEngine.createEngine(appID, appSign, false, ZegoScenario.COMMUNICATION, this, null);
} 
```
#### 2、Choose a role and create a room
Related code can refer to com.zego.chathouse.ui.MainActivity and activity_main.xml The sample code for selecting roles is as follows:
```Java
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
```

 The sample code for entering the chat room page is as follows: 
```Java
private fun enterChatRoom() {
    val intent = Intent(this@MainActivity, ChatRoomActivity::class.java) 
    intent.putExtra(ZegoConstant.ROOM_ID, roomId) 
    intent.putExtra(ZegoConstant.ROLE_TYPE, roleType) 
    startActivity(intent)
} 
```
Note: When exiting the application, you need to de-initialize the SDK, an example is as follows:
```Java
override fun finish() {
    super.finish()
    ZegoExpressEngine.destroyEngine(null)
}
```
#### 3、Chat room
Related code can refer to: com.zego.chathouse.ui.ChatRoomActivity and activity_chat_room
##### 3.1、 Set up event callbacks to the SDK to receive and display various data provided by the SDK
```Java
// Constructor
init {
    mEventHandler = ChatRoomEventHandler()
    mEngine.setEventHandler(mEventHandler) 
}
```
##### 3.2、 Login to the room
```Java 
// receive room id from main activity
mRoomId = intent.getStringExtra(ZegoConstant.ROOM_ID) 
// login room
val config = ZegoRoomConfig()
config.isUserStatusNotify = true
mEngine.loginRoom(mRoomId, mSelfUser, config)
// mute microphone
mEngine.muteMicrophone(true)
// monitor for sound level
mEngine.startSoundLevelMonitor(300)
```
##### 3.3、 Determine whether to push or not according to the current role 
```Java
// receive role type from main activity
mRoleType = intent.getIntExtra(ZegoConstant.ROLE_TYPE, 0) 
if (mRoleType == 0) {
    // is honoured guest, start publish and show toolbar   
    startPublish()
    toolBoxLayout.visibility = View.VISIBLE
} else {
    // is audience, hide tool bar.
    captureSoundLevelImg.visibility = View.GONE
    toolBoxLayout.visibility = View.GONE
} 
``` 
```Java 
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
```
##### 3.4、 Online refresh
When the room staff is updated, the onRoomUserUpdate callback of im.zego.zegoexpress.callback.IZegoEventHandler will be triggered, see it at:
https://doc-en.zego.im/en/api?doc=express_video_sdk_API~Java~class~im-zego-zegoexp ress-callback-i-zego-event-handler#on-engine-state-update
The sample code is as follows:
```Java
override fun onRoomUserUpdate(roomID: String, updateType: ZegoUpdateType, userList: ArrayList<ZegoUser>) {
    super.onRoomUserUpdate(roomID, updateType, userList)
    when {
        ZegoUpdateType.ADD == updateType -> { 
            for (zegoUser in userList) {
                mRoomUsers[zegoUser.userID] = zegoUser
            } 
        }
        ZegoUpdateType.DELETE == updateType -> { 
            for (zegoUser in userList) {
                 mRoomUsers.remove(zegoUser.userID) 
            }
        }
    }
    userCountText.text= getString(R.string.online_user_count, mRoomUsers.size + 1)
} 
```
##### 3.5、 Refresh guest list data and pull guest voice stream
When guests enter the room, they start to push and stop when they exit the room, so the streaming list can be regarded as the guest list. When pushing and stopping the push, the onRoomStreamUpdate callback of im.zego.zegoexpress.callback.IZegoEventHandler will be triggered, see it at:
https://doc-en.zego.im/en/api?doc=express_video_sdk_API~Java~class~im-zego-zegoexp ress-callback-i-zego-event-handler#on-room-stream-update  
```Java
override fun onRoomStreamUpdate(roomID: String?, updateType: ZegoUpdateType, streamList: ArrayList<ZegoStream>, extendedData: JSONObject?) {
    super.onRoomStreamUpdate(roomID, updateType, streamList, extendedData)
     when {
         ZegoUpdateType.ADD == updateType -> {
             for (stream in streamList) {
                  // play stream from honoured guests
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
```
##### 3.6、 start broadcasting, stop broadcasting,
The guest starts to push the stream as soon as they enter the room. When they need to speak, they will go to the microphone (open the microphone), and after the speech is over (turn the microphone off), the example is as follows:
```Java
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
```
 
##### 3.7、 Acoustic effect realization
When collecting volume changes and remote streaming volume changes, the onCapturedSoundLevelUpdate and onRemoteSoundLevelUpdate callbacks of im.zego.zegoexpress.callback.IZegoEventHandler will be triggered, see it at:
https://doc-en.zego.im/en/api?doc=express_video_sdk_API~Java~class~im-zego-zegoexp ress-callback-i-zego-event-handler#on-captured-sound-level-update
And
https://doc-en.zego.im/en/api?doc=express_video_sdk_API~Java~class~im-zego-zegoexp ress-callback-i-zego-event-handler#on-remote-sound-level-update
Sample is : 
```Java
// the sound level of play streams, key-value pair of streamId and sound level
override fun onCapturedSoundLevelUpdate(soundLevel: Float) {
    super.onCapturedSoundLevelUpdate(soundLevel)
    if (0 != mRoleType) {
        return
    }
    runOnUiThread {
        if (soundLevel > 10) {
            captureSoundLevelImg.visibility = View.VISIBLE
        } else {
            captureSoundLevelImg.visibility = View.GONE
        }
    }
}
        
// the sound level of mic
override fun onRemoteSoundLevelUpdate(soundLevels: HashMap<String, Float>) {
    super.onRemoteSoundLevelUpdate(soundLevels)
    mAdapter.update(soundLevels)
}
```
##### 3.8、 Exit the chat room
When you exit the chat room, you need to release resources, an example is as follows:
```Java
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
```

 
