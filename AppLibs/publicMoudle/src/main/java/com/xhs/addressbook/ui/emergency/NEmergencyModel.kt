package com.xhs.addressbook.ui.emergency

class NEmergencyModelReq(
    var pageSize: Int = 20,
    var pageIndex: Int = 1
)

class NEmergencyModelResponse(
    val code: Int = -1,
    val msg: String = "",
    val data: Data = Data()
) {
    class Data(
        val totalcount: Int = 0,
        val mList: List<EmergencyItem> = listOf()
    )
}

class EmergencyItem