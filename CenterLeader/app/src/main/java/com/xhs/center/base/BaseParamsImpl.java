package com.xhs.em_doctor.addressbook;

import com.xhs.baselibrary.init.BaseParamsProvide;
import com.xhs.baselibrary.init.BaseUser;
import com.xhs.em_doctor.BuildConfig;
import com.xhs.em_doctor.baseurlstrategy.BaseUrlController;
import com.xhs.em_doctor.utils.SharedPrefUtils;
import com.xhs.addressbook.utils.sp.UserInformSpUtils;

/**
 * @ author guiyun.li
 * @ Email xyz_6776.@163.com
 * @ date 20/05/2019.
 * description:
 */

public class BaseParamsImpl implements BaseParamsProvide {
    @Override
    public String getUserId() {
        return UserInformSpUtils.getInstance().getUserId();
    }

    @Override
    public String getPhoneNumber() {
        return UserInformSpUtils.getInstance().getPhoneNumber();
    }

    @Override
    public String getHeadHot() {
        return UserInformSpUtils.getInstance().getUserInform().getHeadHot();
    }

    @Override
    public String getPersonName() {
        return UserInformSpUtils.getInstance().getPersonName();
    }

    @Override
    public String getBaseUrl() {
        return BaseUrlController.INSTANCE.getIpAddress("http://192.168.1.88");
    }


    @Override
    public String getNewJavaUrl() {
        return BaseUrlController.INSTANCE.getJavaBaseUrl();
    }

    @Override
    public String getBaseHeadUrl() {
        return BaseUrlController.INSTANCE.getHeadUrl();
    }

    @Override
    public String getToken() {
        return UserInformSpUtils.getInstance().getToken();
    }

    @Override
    public String getAppName() {
        return "doctor";
    }

    @Override
    public int getAreaCode() {
        return BuildConfig.areaCode;
    }

    @Override
    public BaseUser getBaseUser() {
        return new BaseUser(
                UserInformSpUtils.getInstance().getUserId(),
                UserInformSpUtils.getInstance().getPhoneNumber(),
                UserInformSpUtils.getInstance().getPersonName(),
                BaseUrlController.INSTANCE.getHeadUrl() + UserInformSpUtils.getInstance().getUserInform().getHeadHot(),
                UserInformSpUtils.getInstance().getStationId()
        );
    }

    @Override
    public String getDownLoadApkUrl() {
        return BaseUrlController.INSTANCE.getDownLoadApkUrl();
    }

    @Override
    public String getIpAddress() {
        return BaseUrlController.INSTANCE.getIpAddress("");
    }
}
