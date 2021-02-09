package com.zego.chathouse.ui.vo

import im.zego.zegoexpress.entity.ZegoStream

/**
 *
 * @date 2/8/21 7:31 PM
 * @author luke
 * description:
 */
class ChatStreamInfo {
    var userID: String = ""
    var userName: String = ""
    var streamID: String = ""
    var extraInfo: String = ""
    var soundLevel = 0.0f

    constructor(zegoStream: ZegoStream) {
        this.userID = zegoStream.user.userID
        this.userName = zegoStream.user.userName
        this.streamID = zegoStream.streamID
        this.extraInfo = zegoStream.extraInfo
    }
}