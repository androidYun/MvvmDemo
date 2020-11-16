package com.xhs.center.ui.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.xhs.baselibrary.net.mqtt.MqttConfig
import com.xhs.baselibrary.net.mqtt.MqttManager
import com.xhs.center.map.IPositionStrategy
import java.net.URISyntaxException


/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 31/07/2019.
 * description:
 */
class LoadPositionService : Service() {
    /**
     * 可以随时从定位服务里面获取定位信息
     */
    private var serviceBinder: ServiceBinder? = ServiceBinder()

    private val mPositionList = mutableListOf<Any>()

    private val mTemporary = mutableListOf<Any>()

    inner class ServiceBinder : Binder(), ILoadPositionService {
        override fun getLoadPosition(): List<Any> {
            mTemporary.clear()
            mTemporary.addAll(mPositionList)
            mPositionList.clear()
            return mTemporary
        }

        override fun switchSubScribeTopic(mIPosition: IPositionStrategy) {//切换订阅主题
            MqttManager.getInstance().unsubscribe()
            MqttManager.getInstance().subscribe {
                mIPosition.getSubscribeTopic()
                onMessageArrived { _, message, _ ->
                    if (!message.isNullOrBlank()) {
                        mPositionList.add(message)
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        MqttManager.getInstance().init(this, MqttConfig())
        try {
            MqttManager.getInstance().connect {
                onConnectSuccess {
                    MqttManager.getInstance().subscribe() {
                        onConnectSuccess {
                            println("mtqq订阅成功")
                        }

                        onConnectFailed {
                            println("mtqq订阅失败")
                        }
                    }
                    println("mtqq连接成功")
                }
                onConnectFailed {
                    println("mtqq连接失败")
                }
            }
        } catch (e: URISyntaxException) {
            println("mtqq连接失败" + e.message)
        }
        MqttManager.getInstance().subscribe {
            onMessageArrived { _, message, _ ->
                if (!message.isNullOrBlank()) {
                    mPositionList.add(message)
                }
            }
        }
        MqttManager.getInstance().unsubscribe()
        /**
         * 随时切换或者注销监听
         */
    }


    override fun onDestroy() {
        super.onDestroy()
        MqttManager.getInstance().unAllSubscribe()
        MqttManager.getInstance().disconnect()
        MqttManager.getInstance().clear()
        MqttManager.getInstance().close()
    }
}