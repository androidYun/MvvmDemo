package com.xhs.center.presenter.visualzation

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R

interface VisualizationContact {

    interface IVisualizationView : IBaseView {

        fun loadVisualizationSuccess(content: Any)

        fun loadVisualizationFail(throwable: Throwable)

    }

    interface IVisualizationPresenter {
        fun loadVisualization(content: Any)
    }
}