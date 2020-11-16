package com.xhs.em_doctor.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.xhs.baselibrary.utils.PermissionsUtils;
import com.xhs.baselibrary.utils.StatusBarUtil;
import com.xhs.baselibrary.utils.UIUtils;
import com.xhs.em_doctor.DoctorNavigate;
import com.xhs.em_doctor.R;
import com.xhs.em_doctor.base.BaseActivity;
import com.xhs.addressbook.utils.sp.UserInformSpUtils;
import com.xhs.addressbook.bean.UserInformBean;
import com.xhs.em_doctor.view.login.LoginActivity;
import com.xhs.em_doctor.view.login.LoginEnum;
import com.xhs.em_doctor.view.main.MainActivity;

/**
 * 启动页
 * Created by zhf on 2018/10/25 0025.
 */

public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucentForImageView(this, null);
        setContentView(R.layout.activity_splash);
        PermissionsUtils.getInstance().chekPermissions(this, needPermissions, new PermissionsUtils.IPermissionsResult() {
            @Override
            public void passPermissions() {
                jumpActivity();
            }

            @Override
            public void forbidPermissions() {
                Toast.makeText(SplashActivity.this, "您没有允许部分权限，可能会导致部分功能不能正常使用，如需正常使用  请允许权限", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> ActivityCompat.requestPermissions(SplashActivity.this, needPermissions, PermissionsUtils.getInstance().mRequestCode), 500);
            }
        });

    }

    private void jumpActivity() {
        new Handler().postDelayed(() -> {
            if (checkUserData()) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtras(LoginActivity.Companion.getInstance(LoginEnum.NormalLogin.getLoginType()));
                startActivity(intent);
                finish();
            }
        }, 500);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //就多一个参数this
        PermissionsUtils.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private boolean checkUserData() {
        UserInformBean userInform = UserInformSpUtils.getInstance().getUserInform();
        if (TextUtils.isEmpty(userInform.getPhoneNumber()) || TextUtils.isEmpty(userInform.getPassword())) {
            return false;
        }
        return true;
    }

    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
}
