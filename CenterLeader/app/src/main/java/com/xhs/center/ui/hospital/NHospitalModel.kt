package com.xhs.center.ui.hospital

class NHospitalModelReq(
    var pageSize: Int = 20,
    var pageIndex: Int = 1
)

class NHospitalModelResponse(
    val code: Int = -1,
    val msg: String = "",
    val data: Data = Data()
) {
    class Data(
        val totalCount: Int = 0,
        val mList: List<HospitalItem> = listOf()
    )
}

class HospitalItem