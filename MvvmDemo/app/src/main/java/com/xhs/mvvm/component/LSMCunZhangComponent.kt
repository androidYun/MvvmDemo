package com.xhs.mvvm.component

import android.util.Log

class LSMCunZhangComponent(name: String) : ComponentGroup(name) {
    override fun landDivision() {
        Log.d("李桂云", "乱丝庙村长分地${name}")
        mList.forEach { it.landDivision() }
    }
}