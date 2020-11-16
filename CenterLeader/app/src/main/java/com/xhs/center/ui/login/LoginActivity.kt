package com.xhs.em_doctor.view.login

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xhs.addressbook.presenter.check.CheckUpdateContact
import com.xhs.addressbook.presenter.check.CheckUpdatePresenter
import com.xhs.addressbook.presenter.check.VersionBean
import com.xhs.addressbook.presenter.device.NSynchronyDeviceModelReq
import com.xhs.addressbook.presenter.device.SynchronyDeviceContact
import com.xhs.addressbook.presenter.device.SynchronyDevicePresenter
import com.xhs.baselibrary.base.BaseActivity
import com.xhs.baselibrary.dialog.base.CommonDialog.CommonBuilder
import com.xhs.baselibrary.init.BaseParamsClient
import com.xhs.baselibrary.ui.ip.SetIpActivity
import com.xhs.baselibrary.ui.update.UpdateActivity
import com.xhs.baselibrary.utils.DeviceIdUtils
import com.xhs.baselibrary.utils.PermissionsUtils
import com.xhs.baselibrary.utils.PermissionsUtils.IPermissionsResult
import com.xhs.baselibrary.utils.sp.DeviceIdSpUtils
import com.xhs.baselibrary.utils.sp.DeviceIdSpUtils.getDeviceId
import com.xhs.em_doctor.BuildConfig
import com.xhs.em_doctor.R
import com.xhs.em_doctor.baseurlstrategy.BaseUrlController
import com.xhs.em_doctor.utils.KindPhonePermissionUtils
import com.xhs.baselibrary.utils.StatusBarUtil
import com.xhs.em_doctor.view.login.doctor.DoctorLoginFragment
import com.xhs.em_doctor.view.login.expert.ExpertLoginFragment
import kotlinx.android.synthetic.main.activity_login_v2.*

class LoginActivity : BaseActivity(), CheckUpdateContact.ICheckUpdateView, SynchronyDeviceContact.ISynchronyDeviceView {
    private val checkUpdatePresenter by lazy { CheckUpdatePresenter().apply { attachView(this@LoginActivity) } }
    private val synchronyDevicePresenter: SynchronyDevicePresenter by lazy { SynchronyDevicePresenter().apply { attachView(this@LoginActivity) } }
    private val loginType by lazy {
        intent?.extras?.getInt(LOGIN_TYPE_KEY) ?: LoginEnum.NormalLogin.loginType
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setStatusBar(this, Color.WHITE)
        setContentView(R.layout.activity_login_v2)
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        val beginTransaction = supportFragmentManager.beginTransaction()
        if (BaseParamsClient.getInstance().isDoctor) {
            beginTransaction.add(R.id.frameLayout, DoctorLoginFragment.getInstance())
        } else {
            beginTransaction.add(R.id.frameLayout, ExpertLoginFragment.getInstance())
        }
        beginTransaction.commit()
    }

    private fun initData() {
        PermissionsUtils.getInstance().chekPermissions(this, needPermissions, object : IPermissionsResult {
            override fun passPermissions() {

            }

            override fun forbidPermissions() {
                Toast.makeText(this@LoginActivity, "您没有允许部分权限，可能会导致部分功能不能正常使用，如需正常使用  请允许权限", Toast.LENGTH_SHORT).show()
                CommonBuilder(this@LoginActivity)
                        .setMessage(getString(R.string.please_open_obtain_phone_device_permission))
                        .setNegativeButton(getString(R.string.no), null)
                        .setPositiveButton(getString(R.string.yes)) { v: View? -> KindPhonePermissionUtils.jumpPermission(this@LoginActivity) }
                        .setWidthPercentage(0.7)
                        .create().show()
            }
        })
        if (loginType == LoginEnum.NormalLogin.loginType) {
            checkUpdatePresenter.attachView(this)
            checkUpdatePresenter.loadCheckUpdate(BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, BaseParamsClient.getInstance().appName)
            //设备信息同步
            synchronyDevicePresenter.attachView(this)
            val nSynchronyDeviceModelReq = NSynchronyDeviceModelReq()
            nSynchronyDeviceModelReq.imei1 = getDeviceId()
            nSynchronyDeviceModelReq.mixContent = DeviceIdUtils.getDeviceInformJson()
            synchronyDevicePresenter.loadSynchronyDevice(nSynchronyDeviceModelReq)
        }
    }

    private fun initListener() {
        tvSetIp.setOnClickListener {
            val intent = Intent(this, SetIpActivity::class.java)
            startActivity(intent)
        }
        swipeRefresh.setOnRefreshListener {
            val nSynchronyDeviceModelReq = NSynchronyDeviceModelReq()
            nSynchronyDeviceModelReq.imei1 = getDeviceId()
            nSynchronyDeviceModelReq.mixContent = DeviceIdUtils.getDeviceInformJson()
            synchronyDevicePresenter.loadSynchronyDevice(nSynchronyDeviceModelReq)
        }
    }

    override fun loadSynchronyDeviceSuccess(imei1: String) {
        DeviceIdSpUtils.setDeviceId(imei1)
        DeviceIdSpUtils.setSynchronyDeviceInform(true)
        swipeRefresh.isRefreshing = false
    }

    override fun loadSynchronyDeviceFail(throwable: Throwable) {
        handleError(throwable)
        swipeRefresh.isRefreshing = false
    }

    override fun showLoading() {
        showProgressDialog()
    }

    override fun hideLoading() {
        hideProgressDialog()
    }

    override fun loadCheckUpdateSuccess(versionBean: VersionBean) {
        startActivity(Intent(this, UpdateActivity::class.java).putExtras(UpdateActivity.setArgument(
                versionBean.updateContent,
                versionBean.apk,
                versionBean.apkUrl,
                versionBean.forceUpdateFlag !== 0
        )))
    }

    override fun loadCheckUpdateFail(throwable: Throwable) {
        handleError(throwable)
    }

    /**
     * 需要进行检测的权限数组
     */
    private var needPermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    )

    companion object {
        private const val LOGIN_TYPE_KEY = "LOGIN_TYPE_KEY"
        fun getInstance(loginType: Int = LoginEnum.NormalLogin.loginType): Bundle {
            return Bundle().apply { putInt(LOGIN_TYPE_KEY, loginType) }
        }
    }
}