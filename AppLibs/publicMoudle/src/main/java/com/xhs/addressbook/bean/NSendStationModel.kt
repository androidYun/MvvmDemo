package com.xhs.addressbook.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NSendStationModel(
        var centerType: String = "",
        var taskCode: String = "",
        var patientId: String = "",
        var ecgId: String = "",
        var key: String = ""
): Parcelable
