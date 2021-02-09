package com.zego.chathouse.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object ZegoNetUtils {
    /**
     * 没有连接网络
     */
    private const val NETWORK_NONE = -1

    /**
     * 移动网络
     */
    private const val NETWORK_MOBILE = 0

    /**
     * 无线网络
     */
    private const val NETWORK_WIFI = 1

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    fun isNetConnect(context: Context): Boolean {
        return when (getNetWorkType(context)) {
            NETWORK_WIFI -> {
                true
            }
            NETWORK_MOBILE -> {
                true
            }
            NETWORK_NONE -> {
                false
            }
            else -> false
        }
    }

    private fun getNetWorkType(context: Context): Int {
        // 得到连接管理器对象
        val connectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_WIFI
            } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_MOBILE
            }
        } else {
            return NETWORK_NONE
        }
        return NETWORK_NONE
    }

    /**
     * 判断是否有可用网络
     */
    fun existAvailableNetwork(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices) {
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }
}