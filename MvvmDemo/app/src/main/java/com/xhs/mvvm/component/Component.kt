package com.xhs.mvvm.component

abstract class Component(val name: String) {
    //分地
    abstract fun landDivision()

    abstract fun addComponent(mComponent: Component)


    abstract fun removeComponent(mComponent: Component)


    abstract fun getComponent(mComponent: Component): Component?
}