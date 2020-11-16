package com.xhs.addressbook.presenter.update

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.addressbook.api.UpdateFileApi


class UpdateFilePresenter : IPresenter<UpdateFileContact.IUpdateFileView>(),
    UpdateFileContact.IUpdateFilePresenter {
    override fun loadUpdateFile(nUpdateFileModelReq: NUpdateFileModelReq) {
        RetrofitUtils.getRetrofit()
            .create(UpdateFileApi::class.java)
            .loadUpdateFile(nUpdateFileModelReq)
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
                        softView.get()?.loadUpdateFileSuccess(it)
                    } else {
                        softView.get()?.loadUpdateFileFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadUpdateFileFail(throwable) }
            )
    }
}