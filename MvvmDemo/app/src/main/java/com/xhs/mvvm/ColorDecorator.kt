package com.xhs.mvvm

import android.util.Log

class ColorDecorator(private val iDollShape: IDollShape) : DollDecorator(iDollShape) {

    override fun createDoll() {
        super.createDoll()
        createColor()
    }

    private fun createColor() {
        Log.d("李桂云", "添加一个颜色")
    }
}