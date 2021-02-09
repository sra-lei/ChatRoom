package com.zego.chathouse

import android.app.Application
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.constants.ZegoScenario
import im.zego.zegoexpress.entity.ZegoEngineConfig
import java.lang.ref.WeakReference

/**
 *
 * @date 2/8/21 4:08 PM
 * @author luke
 * description:
 */
public class ChatHouseApplication : Application() {
    companion object {
        lateinit var application: WeakReference<Application>
    }

    override fun onCreate() {
        super.onCreate()
        application = WeakReference(this)

        initEngine()
    }

    private fun initEngine() {
        val appID = 3245140400L
        val appSign = "e14f2182e0bcbf7d8beac0c59a7f02f31b6b23c07eec4ba044d20616aa626e0d"

        val config = ZegoEngineConfig()
        config.advancedConfig["prefer_play_ultra_source"] = "1"
        config.advancedConfig["max_channels"] = "50"
        ZegoExpressEngine.setEngineConfig(config)

        ZegoExpressEngine.createEngine(appID, appSign, false, ZegoScenario.COMMUNICATION, this, null);
    }
}