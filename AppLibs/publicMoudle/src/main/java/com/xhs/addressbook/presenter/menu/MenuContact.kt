package com.xhs.addressbook.presenter.menu

import com.xhs.baselibrary.base.IBaseView
import com.xhs.addressbook.R

interface MenuContact {

    interface IMenuView : IBaseView {

        fun loadMenuSuccess(content: Any)

        fun loadMenuFail(throwable: Throwable)

    }

    interface IMenuPresenter {
        fun loadMenu(userId: String)
    }
}