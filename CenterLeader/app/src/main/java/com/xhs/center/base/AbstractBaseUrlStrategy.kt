package com.xhs.em_doctor.baseurlstrategy

import android.os.Build
import com.xhs.baselibrary.utils.sp.IpAddressSpUtils
import com.xhs.em_doctor.BuildConfig

/**
 * @ author guiyun.li
 *
 * @ Email xyz_6776.@163.com
 * @ date 24/07/2019.
 * description:
 */
abstract class AbstractBaseUrlStrategy : BaseUrlStrategy {


    override fun getJavaBaseUrl(): String {
        return getIpAddress().plus("8888/")
    }


    override fun getWebWardUrl(): String {
        return getIpAddress().plus("1337/").plus("phone/normalItem?userId=")
    }

    override fun getWebEcgUrl(): String {
        return getIpAddress().plus("5132/").plus("#/report/phone/")
    }

    override fun getSocketUrl(): String {
        return getIpAddress().plus("1883/")
    }

    override fun getHeadUrl(): String {
        return getIpAddress().plus("7411/headshots/")
    }

    override fun getPictureUrl(): String {
        return getJavaBaseUrl().plus("uploads/images/")
    }

    override fun getVideoUrl(): String {
        return getJavaBaseUrl().plus("uploads/videos/")
    }

    override fun getVoiceUrl(): String {
        return getJavaBaseUrl().plus("uploads/records/")
    }

    override fun getDownLoadApkUrl(): String {
        return getJavaBaseUrl().plus("uploads/apks/")
    }

    override fun getIpNum(): String {
        return IpAddressSpUtils.getIpNum(
                if (BuildConfig.DEBUG) {
                    "192.168.1.88:"
                } else {
                    when (BuildConfig.areaCode) {
                        100 -> //100 是漯河  200 郑州  300新乡  400池州  500 商丘
                            "221.176.230.210:"
                        200 -> //100 是漯河  200 郑州  300新乡  400池州
                            "123.15.35.206:"
                        300 -> "218.28.71.116:"
                        400 -> "60.174.37.59:"
                        500 -> "61.158.170.179:"//商丘
                        600 -> "221.176.159.156:"//开封
                        700 -> "123.15.51.86:"//登封
                        else -> "192.168.1.88:"
                    }
                }
        )
    }
}