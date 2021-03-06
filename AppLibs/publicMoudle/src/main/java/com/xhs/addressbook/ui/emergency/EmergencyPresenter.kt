package com.xhs.addressbook.ui.emergency

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils


class EmergencyPresenter : IPresenter<EmergencyContact.IEmergencyView>(),
    EmergencyContact.IEmergencyPresenter {
    override fun loadEmergency(nEmergencyModelReq: NEmergencyModelReq) {
        RetrofitUtils.getRetrofit()
            .create(EmergencyApi::class.java)
            .loadEmergency(nEmergencyModelReq)
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
                        softView.get()?.loadEmergencySuccess(it.data.mList, it.data.totalCount)
                    } else {
                        softView.get()?.loadEmergencyFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadEmergencyFail(throwable) }
            )
    }
}