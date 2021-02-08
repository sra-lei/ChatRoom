package com.zego.chathouse.ui.base

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.request.ExplainScope
import com.zego.chathouse.R
import com.zego.chathouse.utils.ZegoNetUtils
import org.jetbrains.anko.toast

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionX.init(this)
            .permissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .onExplainRequestReason { scope: ExplainScope, deniedList: List<String?>? ->
                scope.showRequestReasonDialog(
                    deniedList, getString(R.string.tx_has_no_permission),
                    getString(R.string.tx_ok), getString(R.string.tx_quit)
                )
            }
            .request { allGranted: Boolean, _: List<String?>?, _: List<String?>? ->
                if (!allGranted) {
                    toast(getString(R.string.tx_has_no_permission))
                    finish()
                }
            }

        if (!ZegoNetUtils.isNetConnect(this)) {
            Toast.makeText(this, R.string.tx_network_disconnect, Toast.LENGTH_LONG).show()
        }
    }
}