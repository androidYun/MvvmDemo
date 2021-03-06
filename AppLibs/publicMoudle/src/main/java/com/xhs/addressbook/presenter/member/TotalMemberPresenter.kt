package com.xhs.addressbook.presenter.member

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.addressbook.api.TotalMemberApi


class TotalMemberPresenter : IPresenter<TotalMemberContact.ITotalMemberView>(),
    TotalMemberContact.ITotalMemberPresenter {
    override fun loadTotalMember(nTotalMemberModelReq: NTotalMemberModelReq) {
        RetrofitUtils.getRetrofit()
            .create(TotalMemberApi::class.java)
            .loadTotalMember(nTotalMemberModelReq)
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
                        softView.get()?.loadTotalMemberSuccess(it)
                    } else {
                        softView.get()?.loadTotalMemberFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadTotalMemberFail(throwable) }
            )
    }
}