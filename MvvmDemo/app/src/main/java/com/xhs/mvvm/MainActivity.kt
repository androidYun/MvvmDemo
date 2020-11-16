package com.xhs.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xhs.mvvm.component.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val getDistance = GetDistance(34.765091, 113.785113, 34.758601, 113.672282)
        println("测试${getDistance}")
        val intArray = intArrayOf(2, 7, 11, 15)
        twoSum(intArray, 9).forEach {
            println("测试" + it)
        }
    }

    fun twoSum(nums: IntArray, target: Int): IntArray {
        val sum = IntArray(2)
        val toHashSet = nums.toHashSet()
        val map = hashMapOf<Int, Int>()
        nums.mapIndexed { index, i ->
            if (map.containsKey(target - i)) {
                sum[0] = map[target - i] ?: 0
                sum[1] = index
                return sum
            }
            map.put(i, index)
        }
        return sum
    }


    class ListNode(var `val`: Int) {
        var next: ListNode? = null
    }


    private val EARTH_RADIUS = 6378.137;

    private fun rad(d: Double): Double {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     */
    public fun GetDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val radLat1 = rad(lat1);
        val radLat2 = rad(lat2);
        val a = radLat1 - radLat2;
        val b = rad(lng1) - rad(lng2);
        var s = 2 * Math.asin(
            Math.sqrt(
                Math.pow(
                    Math.sin(a / 2),
                    2.0
                ) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2.0)
            )
        );
        s = s * EARTH_RADIUS;
        s = (s * 10000) / 10;
        return s;
    }
}