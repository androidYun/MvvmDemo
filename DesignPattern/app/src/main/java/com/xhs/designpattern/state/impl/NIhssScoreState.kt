package com.xhs.designpattern.state.impl

import com.xhs.designpattern.state.base.IScoreService

class NIhssScoreService : IScoreService {

    override fun loadScoreList(scoreId: String): List<String> {
        println("NIhss评分内容获取评分")
        return mutableListOf("1", "2", "3")
    }

    override fun updateScore(mList: List<String>, scoreId: String) {
        println("NIhss评分内容更新评分")
    }

    override fun getScoreDesc(scoreId: String): String {
        return "病情严重"
    }
    override fun getScoreKey(): String {
        return "Nihss"
    }
}