package com.xhs.mvvm.component

abstract class ComponentGroup( name: String):Component(name) {
    val mList = mutableListOf<Component>()

    override fun addComponent(mComponent: Component) {
        mList.add(mComponent)
    }

    override fun removeComponent(mComponent: Component) {
        mList.add(mComponent)
    }

    override fun getComponent(mComponent: Component): Component? {
        mList.forEach {
            if (mComponent.name == it.name) {
                return it
            }
        }
        return null
    }
}