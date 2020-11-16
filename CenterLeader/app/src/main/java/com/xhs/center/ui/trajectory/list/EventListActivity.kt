package com.xhs.center.ui.trajectory.list

import android.os.Bundle
import com.xhs.baselibrary.base.BaseActivity
import com.xhs.center.R
import kotlinx.android.synthetic.main.activity_event_list.*

class EventListActivity : BaseActivity(), EventListContact.IEventListView {

    private val mEventListPresenter by lazy { EventListPresenter().apply { attachView(this@EventListActivity) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        
    }

    private fun initData() {
        mEventListPresenter.loadEventList("")
    }

    private fun initListener() {
       
    }

    override fun loadEventListSuccess(content: Any) {
        

    }

    override fun loadEventListFail(throwable: Throwable) {
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