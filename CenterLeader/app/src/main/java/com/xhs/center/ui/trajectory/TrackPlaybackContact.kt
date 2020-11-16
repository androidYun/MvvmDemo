package com.xhs.center.ui.trajectory

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R
interface TrackPlaybackContact {

    interface ITrackPlaybackView:IBaseView{

        fun loadTrackPlaybackSuccess(content:Any)

        fun  loadTrackPlaybackFail(throwable: Throwable)

    }

    interface ITrackPlaybackPresenter{
        fun loadTrackPlayback(nTrackPlaybackModelReq: NTrackPlaybackModelReq)
    }
}