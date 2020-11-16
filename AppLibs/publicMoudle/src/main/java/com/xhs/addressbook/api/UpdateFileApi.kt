package com.xhs.addressbook.api

import com.xhs.addressbook.presenter.update.NUpdateFileResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UpdateFileApi {

    /**
     * 上传文件
     *
     * @param fileName
     * @return
     */
    @Multipart
    @POST("v1/headshot")
    fun loadUpdateFile(@Part fileName: MultipartBody.Part?): Observable<NUpdateFileResponse>
}