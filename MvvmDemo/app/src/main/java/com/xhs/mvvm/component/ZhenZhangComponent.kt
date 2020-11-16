package com.xhs.mvvm.component

import android.util.Log

class ZhenZhangComponent(name: String) : ComponentGroup(name) {
    override fun landDivision() {
        Log.d("李桂云", "镇长分地${name}")
        mList.forEach { it.landDivision() }
    }
}