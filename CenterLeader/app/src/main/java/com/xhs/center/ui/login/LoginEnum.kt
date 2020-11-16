package com.xhs.em_doctor.view.login

enum class LoginEnum(val loginType: Int) {
    NormalLogin(0),//正常登录
    ExceptionLogin(1),//异常退出
    ManualControlLogin(2)//正常手动退出
}