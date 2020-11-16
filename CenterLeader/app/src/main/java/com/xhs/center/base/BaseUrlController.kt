package com.xhs.em_doctor.baseurlstrategy

import com.xhs.em_doctor.BuildConfig

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 24/07/2019.
 * description:
 */
object BaseUrlController : BaseUrlStrategy {

    private var baseUrlStrategy: BaseUrlStrategy = if (BuildConfig.DEBUG) {
        DebugBaseUrlStrategy()
    } else {
        ReleaseBaseUrlStrategy()
    }

    fun setBaseUrlStrategy(baseUrlStrategy: BaseUrlStrategy) {
        this.baseUrlStrategy = baseUrlStrategy
    }


    override fun getJavaBaseUrl(): String {
        return baseUrlStrategy.getJavaBaseUrl()
    }


    override fun getIpAddress(defaultIp: String): String {
        return baseUrlStrategy.getIpAddress()
    }

    override fun getWebWardUrl(): String {
        return baseUrlStrategy.getWebWardUrl()
    }

    override fun getWebEcgUrl(): String {
        return baseUrlStrategy.getWebEcgUrl()
    }


    override fun getSocketUrl(): String {
        return baseUrlStrategy.getSocketUrl()
    }

    override fun getHeadUrl(): String {
        return baseUrlStrategy.getHeadUrl()
    }

    override fun getPictureUrl(): String {
        return baseUrlStrategy.getPictureUrl()
    }

    override fun getVideoUrl(): String {
        return baseUrlStrategy.getVideoUrl()
    }

    override fun getVoiceUrl(): String {
        return baseUrlStrategy.getVoiceUrl()
    }

    override fun getDownLoadApkUrl(): String {
        return baseUrlStrategy.getDownLoadApkUrl()
    }

    override fun getIpNum(): String {
        return baseUrlStrategy.getIpNum()
    }


}