package com.zego.chathouse.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.zego.chathouse.ChatHouseApplication
import com.zego.chathouse.R
import java.util.*

/**
 * Copyright © 2016 Zego. All rights reserved.
 * des: Preference helper.
 */
class ZegoPreferenceUtil private constructor() {
    companion object {
        private const val SHARE_PREFERENCE_NAME = "ZEGO_REMOTE_CHECK_VIDEO_CALL"
        private const val KEY_USER_ID = "sp_key_user_id"
        private const val KEY_USER_NAME = "sp_key_user_name"
        private const val KEY_USER_ICON = "sp_key_user_icon"
        private val iconList = mutableListOf(
            R.drawable.if_aunt,
            R.drawable.if_brother,
            R.drawable.if_daughter,
            R.drawable.if_father,
            R.drawable.if_grandfather,
            R.drawable.if_grandmother,
            R.drawable.if_mother,
            R.drawable.if_sister,
            R.drawable.if_son,
            R.drawable.if_uncle,
        )

        /**
         * single instance.
         */
        val instance: ZegoPreferenceUtil = ZegoPreferenceUtil()
    }

    private var mSharedPreferences = ChatHouseApplication.application.get()?.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE)


    /**
     * 获取保存的UserID，如果没有，则新建
     */
    fun getUserID(): String {
        var userID: String = getStringValue(KEY_USER_ID, "")
        if (TextUtils.isEmpty(userID)) {
            userID = UUID.randomUUID().toString()
            userID = userID.replace("-", "")
            // 保存用户名
            setStringValue(KEY_USER_ID, userID)
        }
        return userID
    }

    /**
     * 获取保存的UserName，如果没有，则新建
     */
    fun getUserName(): String {
        var userName: String = getStringValue(KEY_USER_NAME, "")
        if (TextUtils.isEmpty(userName)) {
            // 以设备名称 + 一位随机数  作为用户名
            userName = Build.MODEL + "-" + getUserIconIdx()
            // 保存用户名
            setStringValue(KEY_USER_NAME, userName)
        }
        return userName
    }

    fun getUserIconRes(): Int {
        return iconList[getUserIconIdx()]
    }

    fun getUserIconRes(userName: String): Int {
        try {
            val iconIdx = userName.split("-").last().toInt()
            return getUserIconRes(iconIdx)
        } catch (ex: Exception) {
        }
        return 0
    }

    private fun getUserIconRes(iconIdx: Int): Int {
        return iconList[iconIdx]
    }

    /**
     * 获取保存的UserName，如果没有，则新建
     */
    private fun getUserIconIdx(): Int {
        var userIconIdx = getIntValue(KEY_USER_ICON, 0)
        if (0 == userIconIdx) {
            userIconIdx = Random().nextInt(iconList.size)
            setIntValue(KEY_USER_ICON, userIconIdx)
        }
        return userIconIdx
    }

    fun setStringValue(key: String, value: String) {
        val editor = mSharedPreferences!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(key: String, defaultValue: String): String {
        return mSharedPreferences!!.getString(key, defaultValue) ?: ""
    }

    fun setBooleanValue(key: String?, value: Boolean) {
        val editor = mSharedPreferences!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String?, defaultValue: Boolean): Boolean {
        return mSharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun setIntValue(key: String?, value: Int) {
        val editor = mSharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getIntValue(key: String?, defaultValue: Int): Int {
        return mSharedPreferences!!.getInt(key, defaultValue)
    }

    fun setLongValue(key: String?, value: Long) {
        val editor = mSharedPreferences!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLongValue(key: String?, defaultValue: Long): Long {
        return mSharedPreferences!!.getLong(key, defaultValue)
    }
}