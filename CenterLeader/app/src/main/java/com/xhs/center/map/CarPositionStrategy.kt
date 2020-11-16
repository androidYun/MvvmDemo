package com.xhs.center.map

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class CarPositionState : IPosition {

    override fun dispatchPosition() {
        val subscribe = Observable.interval(30, TimeUnit.SECONDS).subscribe {
            //加载数据 然后是添加还是更新
        }
    }

    override fun getSubscribeTopic(): String {
        return "xhemss/mqtt/app/location"
    }
}