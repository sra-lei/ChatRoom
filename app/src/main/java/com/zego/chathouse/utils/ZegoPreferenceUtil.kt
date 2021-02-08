package com.zego.chathouse.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.zego.chathouse.ChatHouseApplication
import java.text.SimpleDateFormat
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
            val monthAndDay = SimpleDateFormat("MMdd", Locale.CHINA).format(Date())
            // 以设备名称 + 时间日期 + 一位随机数  作为用户名
            userName = Build.MODEL + monthAndDay + Random().nextInt(10)
            // 保存用户名
            setStringValue(KEY_USER_NAME, userName)
        }
        return userName
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