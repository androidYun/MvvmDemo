package com.xhs.center.ui.trajectory

import com.xhs.baselibrary.base.IPresenter
import com.xhs.baselibrary.net.retrifit.RetrofitUtils
import com.xhs.baselibrary.net.util.RxUtils
import com.xhs.center.api.TrackPlaybackApi


class TrackPlaybackPresenter :IPresenter<TrackPlaybackContact.ITrackPlaybackView>(),TrackPlaybackContact.ITrackPlaybackPresenter{
    override fun loadTrackPlayback(nTrackPlaybackModelReq: NTrackPlaybackModelReq) {
        RetrofitUtils.getRetrofit()
            .create(TrackPlaybackApi::class.java)
            .loadTrackPlayback(nTrackPlaybackModelReq)
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
                        softView.get()?.loadTrackPlaybackSuccess(it.data)
                    } else {
                        softView.get()?.loadTrackPlaybackFail(Throwable(it.msg))
                    }
                    //这里面是回调成功的方法
                }, { throwable -> softView.get()?.loadTrackPlaybackFail(throwable) }
            )
    }
}