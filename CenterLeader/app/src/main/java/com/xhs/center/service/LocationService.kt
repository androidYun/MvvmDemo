package com.xhs.em_doctor.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.xhs.baselibrary.App

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 31/07/2019.
 * description:
 */
class LocationService : Service(), AMapLocationListener {
    /**
     * 可以随时从定位服务里面获取定位信息
     */

    private var mLocationClient: AMapLocationClient? = null
    private var mCallbacks: ILocationCallBack? = null

    private val mBinder = object : ILocationService.Stub() {

        override fun unregisterCallback(cb: ILocationCallBack?) {
            if (cb != null) {
                mCallbacks = null
            }
        }

        override fun registerCallback(cb: ILocationCallBack?) {
            if (cb != null) {
                mCallbacks = cb
            }
        }
    }


    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()
        startLocation()
    }

    /**
     * 开启定位
     */
    private fun startLocation() {
        mLocationClient = AMapLocationClient(App.getsInstance())//初始化定位
        mLocationClient?.setLocationListener(this)//设置定位回调监听
        val mLocationOption = AMapLocationClientOption()//初始化定位参数
        //设置定位模式  高精度模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        mLocationOption.isNeedAddress = true//设置是否返回定制信息
        mLocationOption.isOnceLocation = false//设置是否定位一次
        mLocationOption.isWifiScan = true//设置是否强制刷新WIFI
        mLocationOption.isMockEnable = false//设置是否允许模拟位置 NO
        mLocationOption.interval = 10000//设置定位间隔
        mLocationClient?.setLocationOption(mLocationOption)
        mLocationClient?.startLocation()

    }

    override fun onLocationChanged(p0: AMapLocation?) {
        mCallbacks?.setLocation(p0?.latitude ?: 0.0, p0?.longitude ?: 0.0, p0?.speed
                ?: 0.0f, p0?.district ?: "")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mLocationClient != null) {
            println("服务端被销毁")
            mLocationClient?.stopLocation()
            mLocationClient?.onDestroy()
        }
    }
}