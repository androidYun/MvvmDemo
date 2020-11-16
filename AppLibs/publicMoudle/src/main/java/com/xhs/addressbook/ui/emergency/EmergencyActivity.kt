package com.xhs.addressbook.ui.emergency

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.xhs.baselibrary.base.BaseActivity
import com.xhs.addressbook.R
import kotlinx.android.synthetic.main.activity_emergency_layout.*

class EmergencyActivity : BaseActivity(), EmergencyContact.IEmergencyView {

    private val mEmergencyPresenter by lazy { EmergencyPresenter().apply { attachView(this@EmergencyActivity) } }

    private val nEmergencyReq = NEmergencyModelReq()

    private val mEmergencyList = mutableListOf<EmergencyItem>()

    private lateinit var mEmergencyAdapter: EmergencyAdapter

    private var totalCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_layout)
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        mEmergencyAdapter = EmergencyAdapter(mEmergencyList)
        rvEmergency.layoutManager = LinearLayoutManager(this)
        rvEmergency.adapter = mEmergencyAdapter
    }

    private fun initData() {
        mEmergencyPresenter.loadEmergency(nEmergencyReq)
    }

    private fun initListener() {
        swipeEmergency.setOnRefreshListener {
            mEmergencyPresenter.loadEmergency(nEmergencyReq)
        }
        mEmergencyAdapter.setOnLoadMoreListener({
            if (nEmergencyReq.pageIndex * nEmergencyReq.pageSize < totalCount) {
                mEmergencyPresenter.loadEmergency(nEmergencyReq)
            } else {
                mEmergencyAdapter.loadMoreEnd()
            }
        }, rvEmergency)
    }

    override fun loadEmergencySuccess(mList: List<EmergencyItem>, totalCount: Int) {
        if (nEmergencyReq.pageIndex == 1) {
            mEmergencyList.clear()
        }
        this.totalCount = totalCount
        mEmergencyList.addAll(mList)
        mEmergencyAdapter.notifyDataSetChanged()
        mEmergencyAdapter.loadMoreComplete()
        swipeEmergency.isRefreshing = false
        nEmergencyReq.pageIndex++

    }

    override fun loadEmergencyFail(throwable: Throwable) {
        handleError(throwable)
        swipeEmergency.isRefreshing
        mEmergencyAdapter.loadMoreComplete()
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