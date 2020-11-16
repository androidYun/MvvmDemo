package com.xhs.center.map

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class CarPositionStrategy(private val iOperation: IOperationMarker) : IPositionStrategy {

    private var subscribe: Disposable? = null

    private var iPositionStrategy: IPositionStrategy? = null


    override fun startDispatchPosition() {
        //结束上一个任务
        iPositionStrategy?.finishDispatchPosition()
        val iLoadPositionService = iOperation.getILoadPositionService()
        iLoadPositionService.switchSubScribeTopic(this)
        subscribe = Observable.interval(30, TimeUnit.SECONDS).subscribe {
            val loadPosition = iLoadPositionService.getLoadPosition()
            iOperation.clearMarker()
            iOperation.addCarMarker(loadPosition as List<NMarkerModeItem>)
            iOperation.updateMarker(loadPosition as List<NMarkerModeItem>)
        }
    }

    override fun getSubscribeTopic(): String {
        return "xhemss/mqtt/app/location"
    }

    override fun getIOperationMarker(): IOperationMarker {
        return iOperation
    }

    override fun finishDispatchPosition() {
        subscribe?.dispose()
    }

    override fun setPreviousIPositionStrategy(iPositionStrategy: IPositionStrategy) {
        this.iPositionStrategy = iPositionStrategy
    }
}