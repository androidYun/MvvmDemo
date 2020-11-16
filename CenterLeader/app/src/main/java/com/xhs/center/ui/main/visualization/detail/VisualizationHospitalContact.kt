package com.xhs.center.ui.main.visualization.detail

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R
interface VisualizationHospitalContact {

    interface IVisualizationHospitalView:IBaseView{

        fun loadVisualizationHospitalSuccess(content:Any)

        fun  loadVisualizationHospitalFail(throwable: Throwable)

    }

    interface IVisualizationHospitalPresenter{
        fun loadVisualizationHospital(nVisualizationHospitalModelReq: NVisualizationHospitalModelReq)
    }
}