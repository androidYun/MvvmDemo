package com.xhs.designpattern.strategy.base

interface IScoreStrategy {

    fun loadScoreList(scoreId: String): List<String>

    fun updateScore(mList: List<String>, scoreId: String)

    fun getScoreDesc(scoreId: String): String

    fun getScoreKey(): String

}



/**
        主要功能是获取接口操作

 这个策略 里面改变的是 获取接口和集合接口




        **/