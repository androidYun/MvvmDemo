package com.xhs.addressbook.presenter.member

import com.xhs.baselibrary.base.IBaseView
import com.xhs.addressbook.R

interface TotalMemberContact {

    interface ITotalMemberView : IBaseView {

        fun loadTotalMemberSuccess(content: Any)

        fun loadTotalMemberFail(throwable: Throwable)

    }

    interface ITotalMemberPresenter {
        fun loadTotalMember(content: Any)
    }
}