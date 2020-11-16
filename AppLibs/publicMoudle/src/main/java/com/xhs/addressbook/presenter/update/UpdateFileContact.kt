package com.xhs.addressbook.presenter.update

import com.xhs.baselibrary.base.IBaseView
import com.xhs.addressbook.R

interface UpdateFileContact {

    interface IUpdateFileView : IBaseView {

        fun loadUpdateFileSuccess(content: Any)

        fun loadUpdateFileFail(throwable: Throwable)

    }

    interface IUpdateFilePresenter {
        fun loadUpdateFile(content: Any)
    }
}