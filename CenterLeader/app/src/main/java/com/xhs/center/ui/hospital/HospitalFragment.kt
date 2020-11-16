package com.xhs.center.ui.hospital

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.xhs.center.R
import kotlinx.android.synthetic.main.fragment_activity_hospital.*
import com.xhs.baselibrary.base.BaseFragment

class HospitalFragment : BaseFragment(), HospitalContact.IHospitalView {

    private val mHospitalPresenter by lazy { HospitalPresenter().apply { attachView(this@HospitalFragment) } }

    private val nHospitalReq = NHospitalModelReq()

    private val mHospitalList = mutableListOf<HospitalItem>()

    private lateinit var mHospitalAdapter: HospitalAdapter

    private var totalCount = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context)
            .inflate(R.layout.fragment_activity_hospital, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        mHospitalAdapter = HospitalAdapter(mHospitalList)
        rvHospital.layoutManager = LinearLayoutManager(context)
        rvHospital.adapter = mHospitalAdapter
    }

    private fun initData() {
        mHospitalPresenter.loadHospital(nHospitalReq)
    }

    private fun initListener() {
        swipeHospital.setOnRefreshListener {
            mHospitalPresenter.loadHospital(nHospitalReq)
        }
        mHospitalAdapter.setOnLoadMoreListener({
            if (nHospitalReq.pageIndex * nHospitalReq.pageSize < totalCount) {
                mHospitalPresenter.loadHospital(nHospitalReq)
            } else {
                mHospitalAdapter.loadMoreEnd()
            }
        }, rvHospital)
    }

    override fun loadHospitalSuccess(mList: List<HospitalItem>, totalCount: Int) {
        if (nHospitalReq.pageIndex == 1) {
            mHospitalList.clear()
        }
        this.totalCount = totalCount
        mHospitalList.addAll(mList)
        mHospitalAdapter.notifyDataSetChanged()
        mHospitalAdapter.loadMoreComplete()
        swipeHospital.isRefreshing = false
        nHospitalReq.pageIndex++

    }

    override fun loadHospitalFail(throwable: Throwable) {
        handleError(throwable)
        swipeHospital.isRefreshing
        mHospitalAdapter.loadMoreComplete()
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }

    companion object {
        fun getInstance() = HospitalFragment().apply {
            arguments = Bundle().apply {

            }
        }
    }
}