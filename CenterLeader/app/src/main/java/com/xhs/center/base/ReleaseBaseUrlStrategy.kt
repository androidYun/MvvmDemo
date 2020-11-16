package com.xhs.em_doctor.baseurlstrategy

import com.xhs.baselibrary.utils.sp.IpAddressSpUtils
import com.xhs.em_doctor.BuildConfig


/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 24/07/2019.
 * description:
 */
class ReleaseBaseUrlStrategy : AbstractBaseUrlStrategy() {

    override fun getIpAddress(defaultIp: String): String {
        return IpAddressSpUtils.getIpAddress("http://${getIpNum()}")
    }


}