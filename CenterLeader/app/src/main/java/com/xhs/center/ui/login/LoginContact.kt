package com.xhs.center.ui.login

import com.xhs.baselibrary.base.IBaseView
import com.xhs.center.R

interface LoginContact {

    interface ILoginView : IBaseView {

        fun loadLoginSuccess(content: Any)

        fun loadLoginFail(throwable: Throwable)

    }

    interface ILoginPresenter {
        fun loadLogin(content: Any)
    }
}