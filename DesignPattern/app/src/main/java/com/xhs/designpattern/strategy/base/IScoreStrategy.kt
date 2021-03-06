package com.xhs.designpattern.state.base

interface IScoreState {

    fun loadScoreList(scoreId: String): List<String>

    fun updateScore(mList: List<String>, scoreId: String)

    fun getScoreDesc(scoreId: String): String

    fun getScoreKey(): String

}