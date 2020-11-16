package com.xhs.addressbook.presenter.menu

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils


class MenuPresenter : IPresenter<MenuContact.IMenuView>(), MenuContact.IMenuPresenter {
    override fun loadMenu(userId: String) {
        RetrofitUtils.getRetrofit()
                .create(MenuApi::class.java)
                .loadMenu(nMenuModelReq)
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
                                softView.get()?.loadMenuSuccess(it)
                            } else {
                                softView.get()?.loadMenuFail(Throwable(it.msg))
                            }
                            //这里面是回调成功的方法
                        }, { throwable -> softView.get()?.loadMenuFail(throwable) }
                )
    }
}