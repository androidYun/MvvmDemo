package com.xhs.mlecg;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClsUtils {
    public static boolean createBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Boolean returnValue = (Boolean) btClass.getMethod("createBond", new Class[0]).invoke(btDevice, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("绑定：");
        sb.append(returnValue);
        Log.d("bluetooth", sb.toString());
        return returnValue.booleanValue();
    }

    public static boolean removeBond(Class btClass, BluetoothDevice btDevice) throws Exception {
        Boolean returnValue = (Boolean) btClass.getMethod("removeBond", new Class[0]).invoke(btDevice, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("解除绑定：");
        sb.append(returnValue);
        Log.d("bluetooth", sb.toString());
        return returnValue.booleanValue();
    }

    public static boolean setPin(Class btClass, BluetoothDevice btDevice, String str) throws Exception {
        String str2 = "setPin";
        try {
            Boolean bool = (Boolean) btClass.getDeclaredMethod(str2, new Class[]{byte[].class}).invoke(btDevice, new Object[]{str.getBytes()});
            Log.i(str2, "现在设置pin设置成功！");
        } catch (SecurityException e) {
            Log.i(str2, "现在设置pin设置失败！");
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        return true;
    }

    public static boolean autoBond(Class btClass, BluetoothDevice device, String strPin) throws Exception {
        Boolean result = (Boolean) btClass.getMethod("setPin", new Class[]{byte[].class}).invoke(device, new Object[]{strPin.getBytes()});
        StringBuilder sb = new StringBuilder();
        sb.append("自动配对结果:");
        sb.append(result);
        Log.d("bluetooth", sb.toString());
        return result.booleanValue();
    }

    public static byte[] str2HexByte(String data) {
        if (1 == data.length() % 2) {
            return null;
        }
        byte[] li = new byte[(data.length() / 2)];
        for (int i = 0; i < data.length(); i += 2) {
            li[i / 2] = (byte) ((asc2Hex(data.codePointAt(i)) << 4) | asc2Hex(data.codePointAt(i + 1)));
        }
        return li;
    }

    public static int asc2Hex(int data) {
        if (data >= 48 && data <= 57) {
            return data - 48;
        }
        if (data >= 65 && data <= 90) {
            return data - 55;
        }
        if (data < 97 || data > 122) {
            return data;
        }
        return data - 87;
    }

    public static boolean cancelPairingUserInput(Class btClass, BluetoothDevice device) throws Exception {
        Boolean returnValue = (Boolean) btClass.getMethod("cancelPairingUserInput", new Class[0]).invoke(device, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("取消用户输入：");
        sb.append(returnValue);
        Log.d("bluetooth", sb.toString());
        return returnValue.booleanValue();
    }

    public static boolean cancelBondProcess(Class btClass, BluetoothDevice device) throws Exception {
        Boolean returnValue = (Boolean) btClass.getMethod("cancelBondProcess", new Class[0]).invoke(device, new Object[0]);
        StringBuilder sb = new StringBuilder();
        sb.append("取消配对进程：");
        sb.append(returnValue);
        Log.d("bluetooth", sb.toString());
        return returnValue.booleanValue();
    }

    public static void printAllInform(Class clsShow) {
        try {
            Method[] hideMethod = clsShow.getMethods();
            for (int i = 0; i < hideMethod.length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(hideMethod[i].getName());
                sb.append(";and the i is:");
                sb.append(i);
                Log.e("method name", sb.toString());
            }
            Field[] allFields = clsShow.getFields();
            for (Field name : allFields) {
                Log.e("Field name", name.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
