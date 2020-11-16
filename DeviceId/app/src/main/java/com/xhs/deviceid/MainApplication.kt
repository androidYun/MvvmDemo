package com.xhs.mdid

import android.app.Application
import android.util.Log
import com.bun.miitmdid.core.JLibrary

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
//        //移动安全联盟MSA 初始化
//        try {
//            JLibrary.InitEntry(this)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        val miitHelper = MiitHelper(appIdsUpdater)
//        Log.e("++++++ids: ", "++++++ids".plus(miitHelper.getDeviceIds(this)))
    }

    private val appIdsUpdater = MiitHelper.AppIdsUpdater { ids ->
        Log.e("++++++ids: ", ids)
        oaid = ids
    }

    companion object {
        var oaid: String? = null
            private set
        var isSupportOaid = true
            set(isSupportOaid) {
                MainApplication.isSupportOaid = isSupportOaid
            }
        private var errorCode = 0

        fun getErrorCode(): String {
            return errorCode.toString()
        }

        fun setIsSupportOaid(isSupportOaid: Boolean, ErrorCode: Int) {
            MainApplication.isSupportOaid = isSupportOaid
            MainApplication.errorCode = ErrorCode
        }
    }
}