package com.xhs.em_doctor.utils.sp

import com.xhs.em_doctor.service.LocationInform
import com.xhs.em_doctor.view.MapLocationBean

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 05/08/2019.
 * description:
 */
object LocationUtils {

    private val mapLocationBean: MapLocationBean = MapLocationBean()

    fun setLocationInform(locationInform: LocationInform) {
        mapLocationBean.lon = locationInform.lon
        mapLocationBean.lat = locationInform.lat
        mapLocationBean.speed = locationInform.speed
        mapLocationBean.direction = locationInform.direction
    }

    fun getLon(): Double {
        return if (mapLocationBean.lon.isNullOrEmpty()) 0.0 else mapLocationBean.lon.toDouble()
    }


    fun getLat(): Double {
        return if (mapLocationBean.lat.isNullOrEmpty()) 0.0 else mapLocationBean.lat.toDouble()
    }


    fun getSpeed(): String {
        return mapLocationBean.speed
    }


    fun getDirection(): String {
        return mapLocationBean.direction
    }


}