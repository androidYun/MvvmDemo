package com.xhs.mvvm.component

import android.util.Log

class DaGuoCunZhangComponent (name: String) : ComponentGroup(name) {
    override fun landDivision() {
        Log.d("李桂云", "大郭村分地${name}")
        mList.forEach { it.landDivision() }
    }
}