package com.xhs.center.ui.hospital

import android.content.Intent
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.xhs.addressbook.ui.web.WebActivity
import com.xhs.baselibrary.utils.UIUtils
import com.xhs.center.R
import com.xhs.center.ui.trajectory.NTrackPlaybackModel
import com.xhs.center.ui.trajectory.TrackPlaybackActivity
import com.xhs.center.ui.trajectory.list.EventListActivity

class HospitalAdapter(mList: List<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(mList) {
    init {
        addItemType(TYPE_LEVEL_1, R.layout.item_hospital_layout_type_level1)
        addItemType(TYPE_LEVEL_2, R.layout.item_hospital_layout_type_level2)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiItemEntity?) {
        when (helper?.itemViewType) {
            TYPE_LEVEL_1 -> {
                val mHospitalModelItem = item as HospitalItem
                helper?.setText(R.id.tvHospitalName, mHospitalModelItem?.stationName ?: "")
                helper?.setText(R.id.tvHospitalLevel, mHospitalModelItem?.hospitalLevelName ?: "")
                helper?.getView<AppCompatCheckBox>(R.id.cbxSwitchOpen).isChecked =
                    mHospitalModelItem.isExpanded
                helper?.getView<AppCompatCheckBox>(R.id.cbxSwitchOpen).setOnClickListener {
                    val adapterPosition = helper.adapterPosition
                    if (mHospitalModelItem.isExpanded) {
                        mHospitalModelItem.isOpenOrClose = false
                        collapse(adapterPosition) //关闭
                    } else {
                        mHospitalModelItem.isOpenOrClose = true
                        expand(adapterPosition) //展开
                    }
                    helper?.getView<AppCompatCheckBox>(R.id.cbxSwitchOpen).isChecked =
                        mHospitalModelItem.isExpanded
                }
                helper.setVisible(R.id.ivItemCsc, item.isCSC == 0)
                helper.setVisible(R.id.ivItemSfgh, item.isSFGH == 0)
                helper.setVisible(R.id.ivItemCpc, item.isChestPainCenter == 0)
            }
            TYPE_LEVEL_2 -> {
                val mHospitalActualSign = item as HospitalActualSign
                helper?.setText(R.id.tvActualSign, "车辆标识:${mHospitalActualSign.actualSign ?: ""}")
                helper?.setText(R.id.tvPlateNumber, "车牌号码:${mHospitalActualSign.plateNumber ?: ""}")
                helper?.setText(
                    R.id.tvCarState,
                    "${if (!mHospitalActualSign.onlineStatus.isNullOrBlank() && mHospitalActualSign.onlineStatus == "offline ") "是" else "否"}"
                )
                helper?.setTextColor(//在线显示红色
                    R.id.tvCarState,
                    UIUtils.getInstance().getColor(if (!mHospitalActualSign.onlineStatus.isNullOrBlank() && mHospitalActualSign.onlineStatus == "offline ") R.color.color_fb3362 else R.color.color_6)
                )
                helper?.setText(R.id.tvCarDriver, "司机:${mHospitalActualSign.driverName ?: ""}")
                helper?.setText(
                    R.id.tvCallForHelpTime,
                    "呼救时间:${if (!mHospitalActualSign.handleBeginTime.isNullOrBlank()) mHospitalActualSign.handleBeginTime else "当前无急救时间"}"
                )
                helper?.setText(
                    R.id.tvFirstAidAddress,
                    "急救地址:${if (!mHospitalActualSign.emergencyAddress.isNullOrBlank()) mHospitalActualSign.emergencyAddress else "当前无地址"}"
                )
                helper?.getView<TextView>(R.id.tvRealTimeVideo).setOnClickListener {
                    it.context.startActivity(
                        Intent(it.context, WebActivity::class.java).putExtras(
                            WebActivity.setArgument(
                                "http://117.78.0.16:5200/video?phoneNumber=${mHospitalActualSign?.plateNumber
                                    ?: ""}", "车载"
                            )
                        )
                    )
                }
                helper?.getView<TextView>(R.id.tvTrackPlayback).setOnClickListener {
                    it.context.startActivity(
                        Intent(
                            it.context,
                            TrackPlaybackActivity::class.java
                        ).apply {
                            putExtras(
                                TrackPlaybackActivity.getInstance(
                                    NTrackPlaybackModel(
                                        vehicleCode = mHospitalActualSign.vehicleCode ?: "",
                                        handleBeginTime = mHospitalActualSign.handleBeginTime ?: "",
                                        handleEndTime = ""
                                    )
                                )
                            )
                        })
                }
                helper?.getView<TextView>(R.id.tvHistoricalTrack).setOnClickListener {
                    //跳转到事件列表
                    it.context?.startActivity(
                        Intent(
                            it.context,
                            EventListActivity::class.java
                        ).apply {
                            putExtras(
                                EventListActivity.getInstance(
                                    mHospitalActualSign.vehicleCode ?: ""
                                )
                            )
                        })
                }
            }
        }
    }


    companion object {
        const val TYPE_LEVEL_1 = 1
        const val TYPE_LEVEL_2 = 2
    }
}