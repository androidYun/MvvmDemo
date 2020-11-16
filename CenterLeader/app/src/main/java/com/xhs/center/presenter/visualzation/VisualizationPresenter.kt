package com.xhs.center.presenter.visualzation

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.center.api.VisualizationApi


class VisualizationPresenter : IPresenter<VisualizationContact.IVisualizationView>(),
    VisualizationContact.IVisualizationPresenter {
    override fun loadVisualization(nVisualizationModelReq: NVisualizationModelReq) {
        RetrofitUtils.getRetrofit()
            .create(VisualizationApi::class.java)
            .loadVisualization(nVisualizationModelReq)
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
                        softView.get()?.loadVisualizationSuccess(it)
                    } else {
                        softView.get()?.loadVisualizationFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadVisualizationFail(throwable) }
            )
    }
}