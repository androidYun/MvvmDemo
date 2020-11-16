package com.xhs.center.ui.hospital

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils


class HospitalPresenter : IPresenter<HospitalContact.IHospitalView>(),
    HospitalContact.IHospitalPresenter {
    override fun loadHospital(nHospitalModelReq: NHospitalModelReq) {
        RetrofitUtils.getRetrofit()
            .create(HospitalApi::class.java)
            .loadHospital(nHospitalModelReq)
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
                        softView.get()?.loadHospitalSuccess(it.data.mList, it.data.totalCount)
                    } else {
                        softView.get()?.loadHospitalFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadHospitalFail(throwable) }
            )
    }
}