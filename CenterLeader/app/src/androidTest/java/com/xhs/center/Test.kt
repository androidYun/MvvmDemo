package com.xhs.center.map

object Test {
    private val mTotalMarkerList = hashSetOf<NMarkerModeItem>()
    @JvmStatic
    fun main(args: Array<String>) {
        mTotalMarkerList.add(NMarkerModeItem(vehicleCode = "2001"))
        mTotalMarkerList.add(NMarkerModeItem(vehicleCode = "2001"))
        mTotalMarkerList.remove(NMarkerModeItem(vehicleCode = "2001"))
        println("数据就在外面")
    }
}