package com.xhs.mvvm.component

import android.util.Log

class LISonLeaf(name: String) : Component(name) {
    override fun landDivision() {
        Log.d("李桂云", "李家儿子分地${name}")
        Log.d("李桂云", "------------------>>>")
    }

    override fun addComponent(mComponent: Component) {

    }

    override fun removeComponent(mComponent: Component) {

    }

    override fun getComponent(mComponent: Component): Component? {
        return null
    }
}