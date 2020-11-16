package com.xhs.mlecg;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.administrator.Mapper.impl.DeviceMapper;
import com.example.administrator.Utils.ToastUtil;
import com.example.administrator.p004db.DBHelper;
import java.util.ArrayList;
import java.util.Map;

public class DeviceListActivity extends AppCompatActivity {
    private static String DB_NAME = "mydb1";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_Name";
    public static String TAG = DeviceListActivity.class.getName();

    /* renamed from: db */
    public static SQLiteDatabase f37db;
    private String BluetoothName;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 2817;
    /* access modifiers changed from: private */
    public int actionFlag = -1;
    /* access modifiers changed from: private */
    public String address;
    ArrayAdapter<String> adtDevices;
    public String bluetoochAddressStr;
    /* access modifiers changed from: private */
    public BluetoothAdapter bluetoothAdapter;
    private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x012a  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onReceive(android.content.Context r10, Intent r11) {
            /*
                r9 = this;
                java.lang.String r0 = r11.getAction()
                java.lang.String r1 = com.example.administrator.atrecg.DeviceListActivity.TAG
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "action="
                r2.append(r3)
                r2.append(r0)
                java.lang.String r2 = r2.toString()
                android.util.Log.d(r1, r2)
                java.lang.String r1 = ""
                r2 = 0
                int r3 = r0.hashCode()
                r4 = 1167529923(0x459717c3, float:4834.97)
                r5 = 0
                r6 = -1
                r7 = 1
                if (r3 == r4) goto L_0x0039
                r4 = 2116862345(0x7e2cc189, float:5.7408027E37)
                if (r3 == r4) goto L_0x002f
            L_0x002e:
                goto L_0x0043
            L_0x002f:
                java.lang.String r3 = "android.bluetooth.device.action.BOND_STATE_CHANGED"
                boolean r3 = r0.equals(r3)
                if (r3 == 0) goto L_0x002e
                r3 = 1
                goto L_0x0044
            L_0x0039:
                java.lang.String r3 = "android.bluetooth.device.action.FOUND"
                boolean r3 = r0.equals(r3)
                if (r3 == 0) goto L_0x002e
                r3 = 0
                goto L_0x0044
            L_0x0043:
                r3 = -1
            L_0x0044:
                java.lang.String r4 = "android.bluetooth.device.extra.DEVICE"
                if (r3 == 0) goto L_0x012a
                if (r3 == r7) goto L_0x004c
                goto L_0x01d8
            L_0x004c:
                android.os.Parcelable r3 = r11.getParcelableExtra(r4)
                r2 = r3
                android.bluetooth.BluetoothDevice r2 = (android.bluetooth.BluetoothDevice) r2
                int r3 = r2.getBondState()
                java.lang.String r4 = "BlueToothTestActivity"
                switch(r3) {
                    case 10: goto L_0x00ac;
                    case 11: goto L_0x00a5;
                    case 12: goto L_0x005e;
                    default: goto L_0x005c;
                }
            L_0x005c:
                goto L_0x01d8
            L_0x005e:
                java.lang.String r3 = "完成配对"
                android.util.Log.d(r4, r3)
                java.lang.String r3 = "info"
                java.lang.String r4 = "蓝牙设备配对成功！"
                android.util.Log.i(r3, r4)
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                int r3 = r3.actionFlag
                if (r3 != 0) goto L_0x01d8
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                com.example.administrator.Mapper.impl.DeviceMapper r3 = r3.deviceMapper
                android.database.sqlite.SQLiteDatabase r4 = com.example.administrator.atrecg.DeviceListActivity.f37db
                java.lang.String r5 = r2.getName()
                com.example.administrator.atrecg.DeviceListActivity r6 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r6 = r6.address
                r3.insertDeviceMacAddr(r4, r5, r6)
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                if (r3 == 0) goto L_0x0096
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                r3.cancel()
            L_0x0096:
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r4 = r2.getAddress()
                java.lang.String r5 = r2.getName()
                r3.findThenConnect(r4, r5)
                goto L_0x01d8
            L_0x00a5:
                java.lang.String r3 = "正在配对......"
                android.util.Log.d(r4, r3)
                goto L_0x01d8
            L_0x00ac:
                java.lang.String r3 = "取消配对"
                android.util.Log.d(r4, r3)
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                int r3 = r3.actionFlag
                r4 = 0
                if (r3 == 0) goto L_0x00f4
                if (r3 == r7) goto L_0x00be
                goto L_0x01d8
            L_0x00be:
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                com.example.administrator.Mapper.impl.DeviceMapper r3 = r3.deviceMapper
                android.database.sqlite.SQLiteDatabase r5 = com.example.administrator.atrecg.DeviceListActivity.f37db
                java.lang.String r6 = r2.getName()
                com.example.administrator.atrecg.DeviceListActivity r8 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r8 = r8.address
                r3.insertDeviceMacAddr(r5, r6, r8)
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                if (r3 == 0) goto L_0x00e2
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                r3.cancel()
            L_0x00e2:
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r5 = "解除配对成功"
                android.widget.Toast r3 = android.widget.Toast.makeText(r3, r5, r7)
                r3.show()
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                r3.queryButton(r4)
                goto L_0x01d8
            L_0x00f4:
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                com.example.administrator.Mapper.impl.DeviceMapper r3 = r3.deviceMapper
                android.database.sqlite.SQLiteDatabase r5 = com.example.administrator.atrecg.DeviceListActivity.f37db
                java.lang.String r6 = r2.getName()
                com.example.administrator.atrecg.DeviceListActivity r8 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r8 = r8.address
                r3.insertDeviceMacAddr(r5, r6, r8)
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                if (r3 == 0) goto L_0x0118
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                android.app.ProgressDialog r3 = r3.progressDialog
                r3.cancel()
            L_0x0118:
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r5 = "配对失败"
                android.widget.Toast r3 = android.widget.Toast.makeText(r3, r5, r7)
                r3.show()
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                r3.queryButton(r4)
                goto L_0x01d8
            L_0x012a:
                android.os.Parcelable r3 = r11.getParcelableExtra(r4)
                r2 = r3
                android.bluetooth.BluetoothDevice r2 = (android.bluetooth.BluetoothDevice) r2
                java.lang.String r1 = r2.getName()
                com.example.administrator.atrecg.DeviceListActivity r3 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r4 = r2.getAddress()
                r3.deviceAddressStr = r4
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r4 = "找到设备"
                r3.append(r4)
                r3.append(r1)
                java.lang.String r4 = "|"
                r3.append(r4)
                com.example.administrator.atrecg.DeviceListActivity r7 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r7 = r7.deviceAddressStr
                r3.append(r7)
                java.lang.String r3 = r3.toString()
                java.lang.String r7 = "finddevice"
                android.util.Log.i(r7, r3)
                int r3 = r2.getBondState()
                r7 = 10
                if (r3 != r7) goto L_0x019b
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r5 = "未配对|"
                r3.append(r5)
                r3.append(r1)
                r3.append(r4)
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r4 = r4.deviceAddressStr
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.util.ArrayList<java.lang.String> r4 = r4.lstDevices
                int r4 = r4.indexOf(r3)
                if (r4 != r6) goto L_0x0193
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.util.ArrayList<java.lang.String> r4 = r4.lstDevices
                r4.add(r3)
            L_0x0193:
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                android.widget.ArrayAdapter<java.lang.String> r4 = r4.adtDevices
                r4.notifyDataSetChanged()
                goto L_0x01d7
            L_0x019b:
                int r3 = r2.getBondState()
                r7 = 12
                if (r3 != r7) goto L_0x01d7
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r7 = "已配对|"
                r3.append(r7)
                r3.append(r1)
                r3.append(r4)
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.lang.String r4 = r4.deviceAddressStr
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.util.ArrayList<java.lang.String> r4 = r4.lstDevices
                int r4 = r4.indexOf(r3)
                if (r4 != r6) goto L_0x01cf
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                java.util.ArrayList<java.lang.String> r4 = r4.lstDevices
                r4.add(r5, r3)
            L_0x01cf:
                com.example.administrator.atrecg.DeviceListActivity r4 = com.example.administrator.atrecg.DeviceListActivity.this
                android.widget.ArrayAdapter<java.lang.String> r4 = r4.adtDevices
                r4.notifyDataSetChanged()
                goto L_0x01d8
            L_0x01d7:
            L_0x01d8:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.example.administrator.atrecg.DeviceListActivity.C03041.onReceive(android.content.Context, android.content.Intent):void");
        }
    };
    private DBHelper dbHelper;
    public String deviceAddressStr;
    private Map<String, Object> deviceListDataItem;
    DeviceMapper deviceMapper = new DeviceMapper();
    private SimpleAdapter listAdapter;
    ArrayList<String> lstDevices = new ArrayList<>();
    private ListView lvBTDevices;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog = null;
    /* access modifiers changed from: private */
    public BluetoothDevice selectedDevice = null;

    class ItemClickEvent implements OnItemClickListener {
        ItemClickEvent() {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            if (DeviceListActivity.this.bluetoothAdapter.isDiscovering()) {
                DeviceListActivity.this.bluetoothAdapter.cancelDiscovery();
            }
            String[] values = ((String) DeviceListActivity.this.lstDevices.get(position)).split("\\|");
            String address = values[2];
            Log.e("address", values[2]);
            DeviceListActivity deviceListActivity = DeviceListActivity.this;
            deviceListActivity.selectedDevice = deviceListActivity.bluetoothAdapter.getRemoteDevice(address);
            try {
                Boolean valueOf = Boolean.valueOf(false);
                String str = "配对";
                String str2 = "info";
                if (DeviceListActivity.this.selectedDevice.getBondState() == 10) {
                    Log.i(str2, "远程设备发送蓝牙配对请求");
                    DeviceListActivity.this.progressDialog = ProgressDialog.show(DeviceListActivity.this, str, "正在配对，请稍等......");
                    DeviceListActivity.this.actionFlag = 0;
                    ClsUtils.createBond(DeviceListActivity.this.selectedDevice.getClass(), DeviceListActivity.this.selectedDevice);
                } else if (DeviceListActivity.this.selectedDevice.getBondState() == 12) {
                    Log.i(str2, "删除绑定.....");
                    DeviceListActivity.this.progressDialog = ProgressDialog.show(DeviceListActivity.this, str, "正在解除配对，请稍等......");
                    DeviceListActivity.this.actionFlag = 1;
                    ClsUtils.removeBond(DeviceListActivity.this.selectedDevice.getClass(), DeviceListActivity.this.selectedDevice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.device_list);
        setTitle("设备管理");
        if (VERSION.SDK_INT >= 23) {
            String str = "android.permission.ACCESS_COARSE_LOCATION";
            if (checkSelfPermission(str) != 0) {
                requestPermissions(new String[]{str}, 2817);
            }
        }
        init();
        queryButton(null);
    }

    public void queryButton(View view) {
        Log.d("信息", "开始查询信息");
        if (this.bluetoothAdapter.getState() == 10) {
            ToastUtil.showToast(this, "蓝牙适配器已关闭");
            return;
        }
        if (this.bluetoothAdapter.isDiscovering()) {
            this.bluetoothAdapter.cancelDiscovery();
        }
        this.lstDevices.clear();
        Object[] lstDevice = this.bluetoothAdapter.getBondedDevices().toArray();
        for (Object obj : lstDevice) {
            BluetoothDevice device = (BluetoothDevice) obj;
            StringBuilder sb = new StringBuilder();
            sb.append("已配对|");
            sb.append(device.getName());
            sb.append("|");
            sb.append(device.getAddress());
            this.lstDevices.add(sb.toString());
            this.adtDevices.notifyDataSetChanged();
        }
        Log.d(TAG, "bluetoothAdapter.startDiscovery()");
        this.bluetoothAdapter.startDiscovery();
    }

    public void init() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        StringBuilder sb = new StringBuilder();
        sb.append("本机：");
        sb.append(this.bluetoothAdapter.getAddress());
        setTitle(sb.toString());
        this.dbHelper = new DBHelper(this, DB_NAME, null, 1);
        f37db = this.dbHelper.getWritableDatabase();
        this.bluetoochAddressStr = this.deviceMapper.findDeviceMacAddr(f37db);
        this.lvBTDevices = (ListView) findViewById(C0326R.C0328id.searchdevice_listView);
        this.adtDevices = new ArrayAdapter<>(this, 17367043, this.lstDevices);
        this.lvBTDevices.setAdapter(this.adtDevices);
        this.lvBTDevices.setOnItemClickListener(new ItemClickEvent());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.bluetooth.device.action.FOUND");
        intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        Log.d(TAG, "registerReceiver");
        registerReceiver(this.bluetoothBroadcastReceiver, intentFilter);
    }

    public void findThenConnect(String address2, String deviceName) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address2);
        intent.putExtra(EXTRA_DEVICE_NAME, deviceName);
        setResult(-1, intent);
        this.actionFlag = -1;
        finish();
    }
}
