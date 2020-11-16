package com.xhs.center.presenter.station

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R

interface StationContact {

    interface IStationView : IBaseView {

        fun loadStationSuccess(content: Any)

        fun loadStationFail(throwable: Throwable)

    }

    interface IStationPresenter {
        fun loadStation(content: Any)
    }
}