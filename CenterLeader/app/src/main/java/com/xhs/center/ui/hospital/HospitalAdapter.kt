package com.xhs.center.ui.hospital

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xhs.center.R

class HospitalAdapter(mList: List<HospitalItem>) :
    BaseQuickAdapter<HospitalItem, BaseViewHolder>(R.layout.item_activity_hospital_layout, mList) {
    override fun convert(helper: BaseViewHolder?, item: HospitalItem?) {

    }
}