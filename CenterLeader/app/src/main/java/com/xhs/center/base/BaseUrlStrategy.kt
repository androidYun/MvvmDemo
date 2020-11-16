package com.xhs.em_doctor.baseurlstrategy

import com.xhs.em_doctor.BuildConfig

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 24/07/2019.
 * description:
 */
interface BaseUrlStrategy {

    fun getJavaBaseUrl(): String

    fun getIpAddress(defaultIp: String = ""): String

    fun getIpNum(): String

    fun getWebWardUrl(): String

    fun getWebEcgUrl(): String

    fun getSocketUrl(): String

    fun getHeadUrl(): String

    fun getPictureUrl(): String

    fun getVideoUrl(): String

    fun getVoiceUrl(): String

    fun getDownLoadApkUrl(): String

    fun getShareUrl(): String {
        if (BuildConfig.DEBUG) {
            return "http://wxecg.xhemss.com/#/report/wx/"  //测试分享连接
        }
        return when (BuildConfig.areaCode) {
            100 -> //漯河
                "http://lhecg.xhemss.com/#/report/wx/"
            200 -> //郑州
                "http://zzecg.xhemss.com/#/report/wx/"
            300 -> "http://xxecg.xhemss.com/#/report/wx/"
            500 -> "http://sqecg.xhemss.com/#/report/wx/"
            else -> "http://lhecg.xhemss.com/#/report/wx/"
        }
    }
}