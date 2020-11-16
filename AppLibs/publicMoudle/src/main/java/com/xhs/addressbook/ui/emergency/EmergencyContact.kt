package com.xhs.addressbook.ui.emergency

import com.xhs.baselibrary.base.IBaseView
import com.xhs.addressbook.R

interface EmergencyContact {

    interface IEmergencyView : IBaseView {

        fun loadEmergencySuccess(mList: List<EmergencyItem>, totalCount: Int)

        fun loadEmergencyFail(throwable: Throwable)

    }

    interface IEmergencyPresenter {
        fun loadEmergency(nEmergencyModelReq: NEmergencyModelReq)
    }
}