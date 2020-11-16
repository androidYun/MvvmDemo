package com.xhs.center.ui.hospital

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R

interface HospitalContact {

    interface IHospitalView : IBaseView {

        fun loadHospitalSuccess(mList: List<HospitalItem>, totalCount: Int)

        fun loadHospitalFail(throwable: Throwable)

    }

    interface IHospitalPresenter {
        fun loadHospital(nHospitalModelReq: NHospitalModelReq)
    }
}