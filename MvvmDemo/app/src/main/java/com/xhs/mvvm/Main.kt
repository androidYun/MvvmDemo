package com.xhs.mvvm

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val repeatString = test("abcabcbb")
        println("测试${repeatString}")
    }


//    private fun repeatString(str: String): Int {
//        val strList = mutableListOf<String>()
//        var flag = 0
//        var count = 0
//        str.forEachIndexed { index, c ->
//            if (index == 0) {
//                strList.add(c.toString())
//            } else {
//                if (strList.contains(c.toString())) {//如果包含
//                    if (flag < count) {
//                        flag = count
//                    }
//                    val indexOfLast = strList.indexOfFirst { it == c.toString() }
//                    println("测试用例${indexOfLast}")
//                    if (indexOfLast > 0) {
//                        val subList = strList.subList(0, indexOfLast + 1)
//                        strList.removeAll(subList)
//                    }
//                    count = strList.size
//                    strList.add(c.toString())
//                } else {
//                    strList.add(c.toString())
//                }
//
//            }
//            count++
//        }
//
//        if (flag < count) {
//            flag = count
//        }
//        return flag
//    }


    fun test(str: String): Int {//dvddvdrf
        var cauPosition = 0
        var result = 0 //最终位置
        var flag = 0
        str.mapIndexed { index, c ->
            var position = str.indexOf(c, cauPosition)//从开始计算的位置查找当前的字符 或者重复的字符的位置
            if (position < index) {//如果位置小于当前计算的位置 那么 之前有重复字符
                cauPosition = position + 1//在获取第一个位置前面+1
                flag = index - position//开始位置到目前位置是最长的
                if (flag > result) {
                    result = flag
                }
                result = index - position - 1
            }
            result++
        }
        return result
    }


}