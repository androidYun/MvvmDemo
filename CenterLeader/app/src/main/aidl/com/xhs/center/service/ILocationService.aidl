// ILocationService.aidl
package com.xhs.em_doctor.service;
import com.xhs.em_doctor.service.ILocationCallBack;

interface ILocationService {
   void registerCallback(ILocationCallBack cb);
    void unregisterCallback(ILocationCallBack cb);
}
