package com.xhs.mvvm

import android.util.Log

class RealDoll : IDollShape {
    override fun createDoll() {
        Log.d("李桂云", ">>---------------------------------------------->>")
        Log.d("李桂云", "造一个玩偶")
    }
}