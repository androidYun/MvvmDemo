package com.xhs.center.ui.service

import com.xhs.center.map.IPositionStrategy

interface ILoadPositionService {
    fun getLoadPosition(): List<Any>

    fun switchSubScribeTopic(mIPosition: IPositionStrategy)
}
