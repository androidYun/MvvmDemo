package com.xhs.mvvm.component

import android.util.Log
import java.lang.UnsupportedOperationException

class DaGuoSonLeaf(name: String) : Component(name) {
    override fun landDivision() {
        Log.d("李桂云", "大郭村儿子分地${name}")
        Log.d("李桂云", "------------------>>>")
    }

    override fun addComponent(mComponent: Component) {
        throw UnsupportedOperationException("叶节点没有这个功能")
    }

    override fun removeComponent(mComponent: Component) {
        throw UnsupportedOperationException("叶节点没有这个功能")
    }

    override fun getComponent(mComponent: Component): Component? {
        throw UnsupportedOperationException("叶节点没有这个功能")
        return null
    }
}