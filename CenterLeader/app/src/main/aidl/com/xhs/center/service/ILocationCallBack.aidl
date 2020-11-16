// ILocationCallBack.aidl
package com.xhs.em_doctor.service;
import  com.xhs.em_doctor.service.LocationInform;
// Declare any non-default types here with import statements

interface ILocationCallBack {

    void setLocation(double lat,double lon,float speed,String direction);

    LocationInform getLocation();
}
