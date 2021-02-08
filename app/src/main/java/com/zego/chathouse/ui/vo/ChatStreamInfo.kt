package com.zego.chathouse.ui.vo

import im.zego.zegoexpress.entity.ZegoStream
import im.zego.zegoexpress.entity.ZegoUser

/**
 *
 * @date 2/8/21 7:31 PM
 * @author luke
 * description:
 */
data class ChatStreamInfo constructor(var isPlaying: Boolean) : ZegoStream() {
    constructor(zegoUser: ZegoUser) : this(false) {
        this.user = zegoUser
    }

    constructor(zegoStream: ZegoStream) : this(false) {
        this.user = zegoStream.user
        this.isPlaying = false
    }

    private var soundLevel = 0.0f

    fun setSoundLevel(soundLevel: Float) {
        this.soundLevel = soundLevel
    }

    fun getSoundLevel(): Float {
        return soundLevel
    }
}