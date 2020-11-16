package com.xhs.center.ui.main.visualization.detail

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.center.api.VisualizationHospitalApi


class VisualizationHospitalPresenter :IPresenter<VisualizationHospitalContact.IVisualizationHospitalView>(),VisualizationHospitalContact.IVisualizationHospitalPresenter{
    override fun loadVisualizationHospital(nVisualizationHospitalModelReq: NVisualizationHospitalModelReq) {
        RetrofitUtils.getRetrofit()
            .create(VisualizationHospitalApi::class.java)
            .loadVisualizationHospital(nVisualizationHospitalModelReq)
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
                        softView.get()?.loadVisualizationHospitalSuccess(it.data)
                    } else {
                        softView.get()?.loadVisualizationHospitalFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadVisualizationHospitalFail(throwable) }
            )
    }
}