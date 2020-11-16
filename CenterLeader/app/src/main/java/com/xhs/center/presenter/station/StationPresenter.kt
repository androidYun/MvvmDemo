package com.xhs.center.presenter.station

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.center.api.StationApi


class StationPresenter : IPresenter<StationContact.IStationView>(),
    StationContact.IStationPresenter {
    override fun loadStation(nStationModelReq: NStationModelReq) {
        RetrofitUtils.getRetrofit()
            .create(StationApi::class.java)
            .loadStation(nStationModelReq)
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
                        softView.get()?.loadStationSuccess(it)
                    } else {
                        softView.get()?.loadStationFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadStationFail(throwable) }
            )
    }
}