package com.xhs.center.ui.main.visualization.detail

import android.os.Bundle
import com.xhs.baselibrary.base.BaseActivity
import com.xhs.center.R
import kotlinx.android.synthetic.main.activity_visualization_hospital.*

class VisualizationHospitalActivity : BaseActivity(), VisualizationHospitalContact.IVisualizationHospitalView {

    private val mVisualizationHospitalPresenter by lazy { VisualizationHospitalPresenter().apply { attachView(this@VisualizationHospitalActivity) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualization_hospital)
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        
    }

    private fun initData() {
        mVisualizationHospitalPresenter.loadVisualizationHospital("")
    }

    private fun initListener() {
       
    }

    override fun loadVisualizationHospitalSuccess(content: Any) {
        

    }

    override fun loadVisualizationHospitalFail(throwable: Throwable) {
        handleError(throwable)
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }
    companion object {
        fun getInstance() = Bundle().apply { }
    }
}