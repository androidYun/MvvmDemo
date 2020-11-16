package com.xhs.mvvm

abstract class DollDecorator(private val iDollShape: IDollShape) : IDollShape {

    override fun createDoll() {
        iDollShape.createDoll()
    }
}