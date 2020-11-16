package com.xhs.mvvm.component

import android.util.Log

class LaoLiComponent(name: String) : ComponentGroup(name) {

    override fun landDivision() {
        Log.d("李桂云", "分地${name}")
        mList.forEach { it.landDivision() }
    }
}