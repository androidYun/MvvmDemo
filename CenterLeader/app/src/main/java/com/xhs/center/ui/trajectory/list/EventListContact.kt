package com.xhs.center.ui.trajectory.list

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R
interface EventListContact {

    interface IEventListView:IBaseView{

        fun loadEventListSuccess(content:Any)

        fun  loadEventListFail(throwable: Throwable)

    }

    interface IEventListPresenter{
        fun loadEventList(nEventListModelReq: NEventListModelReq)
    }
}