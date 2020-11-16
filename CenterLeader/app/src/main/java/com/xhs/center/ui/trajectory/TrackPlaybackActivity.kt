package com.xhs.center.ui.trajectory

import android.os.Bundle
import com.xhs.baselibrary.base.BaseActivity
import com.xhs.center.R
import kotlinx.android.synthetic.main.activity_track_play_back.*

class TrackPlaybackActivity : BaseActivity(), TrackPlaybackContact.ITrackPlaybackView {

    private val mTrackPlaybackPresenter by lazy { TrackPlaybackPresenter().apply { attachView(this@TrackPlaybackActivity) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_play_back)
        initView()
        initData()
        initListener()
    }


    private fun initView() {
        
    }

    private fun initData() {
        mTrackPlaybackPresenter.loadTrackPlayback("")
    }

    private fun initListener() {
       
    }

    override fun loadTrackPlaybackSuccess(content: Any) {
        

    }

    override fun loadTrackPlaybackFail(throwable: Throwable) {
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