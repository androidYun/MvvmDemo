package com.xhs.center.map

import com.xhs.baselibrary.net.util.RxUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MemberPositionStrategy(private val iOperation: IOperationMarker) : IPositionStrategy {

    private var subscribe: Disposable? = null

    private var iPositionStrategy: IPositionStrategy? = null

    /**
     * 1,进来先清除所有marker
     * 2，查询中列表中不存在的数据
     * 3，查询中存在的数据，并提炼出来
     *
     *
     */
    //集合中所有内容
    private val mTotalMarkerList = hashSetOf<NMarkerModeItem>()

    override fun startDispatchPosition() {
        //结束上一个任务
        iPositionStrategy?.finishDispatchPosition()
        val iLoadPositionService = iOperation.getILoadPositionService()
        iLoadPositionService?.switchSubScribeTopic(this)
        iOperation.clearMarker()
        subscribe = Observable.interval(30, TimeUnit.SECONDS)
            .compose(RxUtils.getSchedulerTransformer())
            .subscribe {
                val mNewPosition = iLoadPositionService?.getLoadPosition() ?: listOf()
                println("接受数据${mNewPosition.size}")
                val sameList = mNewPosition.intersect(mTotalMarkerList).toList()
                //取出不相同的数据
                val unionList = mNewPosition.subtract(mTotalMarkerList).toList()
                mTotalMarkerList.addAll(unionList)//添加没有的数据
                mTotalMarkerList.addAll(sameList)//替换之前的数据
                if (!unionList.isNullOrEmpty()) {
                    iOperation.addMemberMarker(unionList)
                }
                if (!sameList.isNullOrEmpty()) {
                    iOperation.updateMarker(sameList)
                }

            }
    }

    override fun getSubscribeTopic(): String {
        return "xhemss/mqtt/app/location"
    }

    override fun getIOperationMarker(): IOperationMarker {
        return iOperation
    }

    override fun finishDispatchPosition() {
        println("人员定时被关闭")
        subscribe?.dispose()
    }

    override fun setPreviousIPositionStrategy(iPositionStrategy: IPositionStrategy?) {
        this.iPositionStrategy = iPositionStrategy
    }

    override fun loadPreviousIPositionStrategy(): IPositionStrategy? {
        return iPositionStrategy
    }
}