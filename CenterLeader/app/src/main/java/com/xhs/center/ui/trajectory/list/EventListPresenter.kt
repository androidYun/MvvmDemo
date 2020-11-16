package com.xhs.center.ui.trajectory.list

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.center.api.EventListApi


class EventListPresenter :IPresenter<EventListContact.IEventListView>(),EventListContact.IEventListPresenter{
    override fun loadEventList(nEventListModelReq: NEventListModelReq) {
        RetrofitUtils.getRetrofit()
            .create(EventListApi::class.java)
            .loadEventList(nEventListModelReq)
            .compose(RxUtils.getSchedulerTransformer())
            .compose(RxUtils.bindToLifecycle(softView.get()))
            .doOnSubscribe { disposable ->
                addDisposable(disposable)
                softView.get()?.showLoading()
            }.doFinally {
                softView.get()?.hideLoading()
                onStop()
            }
            .subscribe(
                {
                    if (it.code == 200) {
                        softView.get()?.loadEventListSuccess(it.data)
                    } else {
                        softView.get()?.loadEventListFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadEventListFail(throwable) }
            )
    }
}