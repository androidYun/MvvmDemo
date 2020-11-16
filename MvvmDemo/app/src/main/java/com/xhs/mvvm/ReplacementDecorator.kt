package com.xhs.mvvm

import android.util.Log

class ReplacementDecorator(private val iDollShape: IDollShape) : DollDecorator(iDollShape) {

    override fun createDoll() {
        super.createDoll()
        createReplacement()
    }

    private fun createReplacement() {
        Log.d("李桂云", "添加一个配件")
    }
}