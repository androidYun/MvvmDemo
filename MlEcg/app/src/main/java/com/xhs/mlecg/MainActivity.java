package com.xhs.mlecg;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.Controller.FileController;
import com.example.administrator.Controller.SystemController;
import com.example.administrator.Controller.userController;
import com.example.administrator.Mapper.impl.DeviceMapper;
import com.example.administrator.Utils.Base64Util;
import com.example.administrator.Utils.JsonUtil;
import com.example.administrator.Utils.PreferencesService;
import com.example.administrator.p004db.DBHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private static String BluetoochAddress = null;
    private static String DB_NAME = "mydb1";
    private static final int MSG_NEW_DATA = 3;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_PATIENT_CHOICE = 2;
    public static final String SET_INFO = "SET_info";
    private static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final String TAG = "MainActivity";
    public static final String USERNAME = "USERNAME";
    public static String authToken;

    /* renamed from: db */
    public static SQLiteDatabase f39db;
    public static String diagnosingFilename;
    public static int diagnosingId = 0;
    public static String diagnosingPatientBirthday;
    public static int diagnosingPatientId;
    public static String diagnosingPatientName;
    public static String diagnosingPatientPhone;
    public static String diagnosingPatientSex;
    public static String diagnosingTime;
    public static String doctorName;
    public static String employment;
    public static int sampleTime = 7550;
    public static int tag = 0;
    public static Integer userId = Integer.valueOf(-1);
    private final int ASCII = 2;
    /* access modifiers changed from: private */
    public byte[] BaselinedriftValue = {0, 1, 3};
    private final int DEC = 1;
    private byte Data5;
    private byte Data6;
    DeviceMapper DeviceMapper = new DeviceMapper();
    private byte EcgCmd;
    private final int HEX = 0;
    private String MsgStr = "";
    /* access modifiers changed from: private */
    public byte[] MyoelectricValue = {0, 2, 4};
    /* access modifiers changed from: private */
    public byte[] NoiseValue = {0, 1, 2};
    public boolean SendStatus;
    private alertMessage altmsg = new alertMessage();
    boolean bAskUpLoad = false;
    private byte[] bLeadStatus;
    public boolean bRecUDP;
    private boolean bTestFlag;
    private DBHelper dbHelper;
    public DrawSample drawSample;
    FileController fileController = new FileController();
    /* access modifiers changed from: private */
    public int iBaselinedriftFilter = 0;
    /* access modifiers changed from: private */
    public int iMyoelectricFilter = 0;
    private short[] iNew18LeadData;
    /* access modifiers changed from: private */
    public int iNioseFilter = 0;
    private short[] iOld18LeadData;
    public int iPatientAge;
    public int iPatientSex;
    private short[] iRecData;
    private int iRecDataNum;
    public short[] iRecOneData;
    private short iRecStatus;
    int iSelect;
    /* access modifiers changed from: private */
    public boolean isPause = false;
    private Boolean listenStatus = Boolean.valueOf(false);
    LinearLayout llTab1;
    LinearLayout llTab2;
    LinearLayout llTab3;
    LinearLayout llTab4;
    LinearLayout llTab5;
    public BluetoothAdapter mBluetoothAdapter;
    /* access modifiers changed from: private */
    public List<Integer> mBuffer;
    private int mCodeType = 0;
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    public ConnectedThread mConnectedThread;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3 && !MainActivity.this.isPause) {
                synchronized (MainActivity.this.mBuffer) {
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public int m_iLeadNum;
    /* access modifiers changed from: private */
    public PreferencesService service;
    private DatagramSocket socketRec;
    private DatagramSocket socketSend;
    String[] strBaselinedriftFilter = {"150Hz", "42Hz", "26Hz"};
    String[] strMyoelectricFilter = {"3.3s", "1.26s", "0.31"};
    String[] strNoiseFilter = {"关", "50Hz", "60Hz"};
    public String strPatientName;
    SystemController systemController = new SystemController();
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    private List<TextView> tv_list;
    TextView txt;
    userController userController = new userController();

    private class ConnectThread extends Thread {
        public boolean connState = false;
        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
        }

        public void run() {
            BluetoothSocket tmp = null;
            try {
                tmp = this.mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(MainActivity.SPP_UUID));
                MainActivity.this.WriteMsg("1、创建refcommSocket成功,创建一个bluetoothSocket,（1032）");
            } catch (IOException e) {
                MainActivity mainActivity = MainActivity.this;
                StringBuilder sb = new StringBuilder();
                sb.append("1.e、创建refcommSocket失败（1034）");
                sb.append(e.getMessage());
                mainActivity.WriteMsg(sb.toString());
            }
            this.mmSocket = tmp;
            setName("ConnectThread");
            if (MainActivity.this.mBluetoothAdapter.isDiscovering()) {
                MainActivity.this.mBluetoothAdapter.cancelDiscovery();
            }
            try {
                this.mmSocket.connect();
                MainActivity.this.WriteMsg("2、Socket开始连接！1047");
                MainActivity.tag = 1;
                MainActivity.this.mConnectThread = null;
                MainActivity.this.invalidateOptionsMenu();
                MainActivity mainActivity2 = MainActivity.this;
                mainActivity2.mConnectedThread = new ConnectedThread(this.mmSocket);
                MainActivity.this.mConnectedThread.start();
            } catch (IOException e2) {
                MainActivity.tag = 0;
                MainActivity mainActivity3 = MainActivity.this;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("2.e、Socket出错！1053");
                sb2.append(e2.getMessage());
                mainActivity3.WriteMsg(sb2.toString());
                try {
                    this.mmSocket.close();
                    MainActivity.this.WriteMsg("2.1e、尝试关闭Socket 1056");
                } catch (IOException e3) {
                    MainActivity.this.WriteMsg("2.2e、尝试关闭Socket 失败 1059");
                }
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "close() of connect socket failed", e);
            }
            MainActivity.tag = 0;
            MainActivity.this.invalidateOptionsMenu();
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            MainActivity.this.WriteMsg("3、create ConnectedThread（1094）");
            this.mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                MainActivity.this.WriteMsg("3.1、创建读写流成功,1103）");
            } catch (IOException e) {
                MainActivity.this.WriteMsg("3.1.e.1、创建读写流出错,1105）");
            }
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            MainActivity.this.WriteMsg("BEGIN mConnectedThread");
            byte[] buffer = new byte[2560];
            while (true) {
                try {
                    MainActivity.this.ProcessDeviceData(buffer, this.mmInStream.read(buffer));
                } catch (IOException e) {
                    Log.e(MainActivity.TAG, "数据连接出错，disconnected", e);
                    MainActivity.tag = 0;
                    MainActivity.this.invalidateOptionsMenu();
                    return;
                }
            }
        }

        public void write(byte[] buffer) {
            try {
                this.mmOutStream.write(buffer);
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(MainActivity.TAG, "close() of connect socket failed", e);
            }
        }
    }

    public class alertMessage {
        public alertMessage() {
        }

        public void popmessage(String message) {
            try {
                Toast.makeText(MainActivity.this, message, 1).show();
            } catch (Exception e) {
                try {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, message, 1).show();
                } catch (Exception e2) {
                }
            }
        }
    }

    static {
        String str = "";
        authToken = str;
        doctorName = str;
        employment = str;
        BluetoochAddress = str;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.activity_main);
        this.bTestFlag = false;
        initComponent();
        inint();
    }

    public String getFomartDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /* access modifiers changed from: private */
    public void WriteMsg(String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.MsgStr);
        sb.append(msg);
        sb.append(" ");
        sb.append(getFomartDate("yyyy-MM-dd HH:mm:ss"));
        sb.append("\n");
        this.MsgStr = sb.toString();
        Log.i(TAG, this.MsgStr);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        if (!this.mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.bAskUpLoad) {
            this.bAskUpLoad = false;
            uploadData();
            return super.onPrepareOptionsMenu(menu);
        }
        int i = tag;
        if (i == 1) {
            MenuItem item = menu.getItem(0);
            StringBuilder sb = new StringBuilder();
            sb.append("（已连接ECG:");
            sb.append(BluetoochAddress);
            sb.append("）");
            item.setTitle(sb.toString());
            menu.getItem(1).setEnabled(false);
            SendCMD(this.m_iLeadNum);
        } else if (i == 0) {
            menu.getItem(0).setTitle("（未连接ECG）");
            menu.getItem(1).setEnabled(true);
        }
        MenuItem item2 = menu.getItem(4);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("设置采样时长：");
        sb2.append(sampleTime / 500);
        sb2.append("秒");
        item2.setTitle(sb2.toString());
        MenuItem item3 = menu.getItem(5);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("抗噪滤波：");
        sb3.append(this.strNoiseFilter[this.iNioseFilter]);
        item3.setTitle(sb3.toString());
        MenuItem item4 = menu.getItem(6);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("基漂滤波：");
        sb4.append(this.strBaselinedriftFilter[this.iBaselinedriftFilter]);
        item4.setTitle(sb4.toString());
        MenuItem item5 = menu.getItem(7);
        StringBuilder sb5 = new StringBuilder();
        sb5.append("肌电滤波：");
        sb5.append(this.strMyoelectricFilter[this.iMyoelectricFilter]);
        item5.setTitle(sb5.toString());
        menu.getItem(10).setVisible(this.bTestFlag);
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == C0326R.C0328id.Set) {
            Intent intent = new Intent();
            intent.setClass(this, page2.class);
            startActivityForResult(intent, 0);
        }
        if (id == C0326R.C0328id.Reset) {
            BluetoothEcgCMD(53, 0, 0, 0);
            for (int j = 0; j < 14; j++) {
                this.iOld18LeadData[j] = 2048;
            }
        }
        if (id == C0326R.C0328id.ConnectDevice) {
            this.MsgStr = "";
            if (tag == 0) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("0、开始连接蓝牙设备地址为：");
                    sb.append(BluetoochAddress);
                    WriteMsg(sb.toString());
                    boolean connectDevice = connectDevice(BluetoochAddress);
                    if (tag == 1) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("蓝牙设备连接成功！");
                        sb2.append(BluetoochAddress);
                        Toast.makeText(this, sb2.toString(), 0).show();
                    } else {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("蓝牙设备连接失败！");
                        sb3.append(BluetoochAddress);
                        Toast.makeText(this, sb3.toString(), 0).show();
                    }
                } catch (Exception ee) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("蓝牙设备连接出错：");
                    sb4.append(ee.getMessage());
                    sb4.append("地址：");
                    sb4.append(BluetoochAddress);
                    sb4.append("请检查蓝牙设备是否开启");
                    Toast.makeText(this, sb4.toString(), 0).show();
                }
            }
            Toast.makeText(this, this.MsgStr, 1).show();
        }
        String str = "确定";
        if (id == C0326R.C0328id.SetSampleTime) {
            Builder builder = new Builder(this);
            builder.setTitle("设置采样时长");
            int checkedItem = 0;
            int i = sampleTime;
            if (i == 5050) {
                checkedItem = 0;
            } else if (i == 7550) {
                checkedItem = 1;
            } else if (i == 10050) {
                checkedItem = 2;
            } else if (i == 15050) {
                checkedItem = 3;
            } else if (i == 30050) {
                checkedItem = 4;
            } else if (i == 60050) {
                checkedItem = 5;
            } else if (i == 90050) {
                checkedItem = 6;
            }
            builder.setSingleChoiceItems(new String[]{"10秒", "15秒", "20秒", "30秒", "60秒", "120秒", "180秒"}, checkedItem, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.iSelect = which;
                }
            });
            builder.setPositiveButton(str, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (MainActivity.this.iSelect) {
                        case 0:
                            MainActivity.sampleTime = 5050;
                            break;
                        case 1:
                            MainActivity.sampleTime = 7550;
                            break;
                        case 2:
                            MainActivity.sampleTime = 10050;
                            break;
                        case 3:
                            MainActivity.sampleTime = 15050;
                            break;
                        case 4:
                            MainActivity.sampleTime = 30050;
                            break;
                        case 5:
                            MainActivity.sampleTime = 60050;
                            break;
                        case 6:
                            MainActivity.sampleTime = 90050;
                            break;
                    }
                    MainActivity.this.invalidateOptionsMenu();
                    System.out.println(MainActivity.sampleTime);
                    MainActivity.this.service.saveconfig(MainActivity.sampleTime, MainActivity.this.iNioseFilter, MainActivity.this.iBaselinedriftFilter, MainActivity.this.iMyoelectricFilter, MainActivity.this.m_iLeadNum);
                }
            });
            builder.create().show();
        }
        if (id == C0326R.C0328id.NoiseFilter) {
            Builder builder2 = new Builder(this);
            builder2.setTitle("抗噪滤波");
            int checkedItem2 = this.iNioseFilter;
            String[] strArr = this.strNoiseFilter;
            builder2.setSingleChoiceItems(new String[]{strArr[0], strArr[1], strArr[2]}, checkedItem2, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.iSelect = which;
                }
            });
            builder2.setPositiveButton(str, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.iNioseFilter = mainActivity.iSelect;
                    MainActivity.this.invalidateOptionsMenu();
                    MainActivity.this.service.saveconfig(MainActivity.sampleTime, MainActivity.this.iNioseFilter, MainActivity.this.iBaselinedriftFilter, MainActivity.this.iMyoelectricFilter, MainActivity.this.m_iLeadNum);
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.BluetoothEcgCMD(37, mainActivity2.NoiseValue[MainActivity.this.iNioseFilter], MainActivity.this.BaselinedriftValue[MainActivity.this.iBaselinedriftFilter], MainActivity.this.MyoelectricValue[MainActivity.this.iMyoelectricFilter]);
                }
            });
            builder2.create().show();
        }
        if (id == C0326R.C0328id.BaselinedriftFilter) {
            Builder builder3 = new Builder(this);
            builder3.setTitle("基漂滤波");
            int checkedItem3 = this.iBaselinedriftFilter;
            String[] strArr2 = this.strBaselinedriftFilter;
            builder3.setSingleChoiceItems(new String[]{strArr2[0], strArr2[1], strArr2[2]}, checkedItem3, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.iSelect = which;
                }
            });
            builder3.setPositiveButton(str, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.iBaselinedriftFilter = mainActivity.iSelect;
                    MainActivity.this.invalidateOptionsMenu();
                    MainActivity.this.service.saveconfig(MainActivity.sampleTime, MainActivity.this.iNioseFilter, MainActivity.this.iBaselinedriftFilter, MainActivity.this.iMyoelectricFilter, MainActivity.this.m_iLeadNum);
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.BluetoothEcgCMD(37, mainActivity2.NoiseValue[MainActivity.this.iNioseFilter], MainActivity.this.BaselinedriftValue[MainActivity.this.iBaselinedriftFilter], MainActivity.this.MyoelectricValue[MainActivity.this.iMyoelectricFilter]);
                }
            });
            builder3.create().show();
        }
        if (id == C0326R.C0328id.MyoelectricFilter) {
            Builder builder4 = new Builder(this);
            builder4.setTitle("肌电滤波");
            int checkedItem4 = this.iMyoelectricFilter;
            String[] strArr3 = this.strMyoelectricFilter;
            builder4.setSingleChoiceItems(new String[]{strArr3[0], strArr3[1], strArr3[2]}, checkedItem4, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.iSelect = which;
                }
            });
            builder4.setPositiveButton(str, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity mainActivity = MainActivity.this;
                    mainActivity.iMyoelectricFilter = mainActivity.iSelect;
                    MainActivity.this.invalidateOptionsMenu();
                    MainActivity.this.service.saveconfig(MainActivity.sampleTime, MainActivity.this.iNioseFilter, MainActivity.this.iBaselinedriftFilter, MainActivity.this.iMyoelectricFilter, MainActivity.this.m_iLeadNum);
                    MainActivity mainActivity2 = MainActivity.this;
                    mainActivity2.BluetoothEcgCMD(37, mainActivity2.NoiseValue[MainActivity.this.iNioseFilter], MainActivity.this.BaselinedriftValue[MainActivity.this.iBaselinedriftFilter], MainActivity.this.MyoelectricValue[MainActivity.this.iMyoelectricFilter]);
                }
            });
            builder4.create().show();
        }
        if (id == C0326R.C0328id.device_manager) {
            Intent intent2 = new Intent();
            intent2.setClass(this, DeviceListActivity.class);
            startActivityForResult(intent2, 1);
        }
        if (id == C0326R.C0328id.ecg_help) {
            new Builder(this).setTitle("操作指南").setMessage("左滑屏：减小走纸速度\n右滑屏：增加走纸速度\n上滑屏：增加增益\n下滑屏：减小增益").setPositiveButton(str, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).show();
        }
        if (id == C0326R.C0328id.ecg_test) {
            DrawSample drawSample2 = this.drawSample;
            drawSample2.iRecTestNum = 0;
            drawSample2.iErrTestNum = 0;
            drawSample2.bTestFlag = true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean connect(BluetoothDevice device) {
        StringBuilder sb = new StringBuilder();
        sb.append("connect to: ");
        sb.append(device);
        Log.d(TAG, sb.toString());
        try {
            this.mConnectThread = new ConnectThread(device);
            this.mConnectThread.start();
            return true;
        } catch (Exception ee) {
            Log.e("MainActivity394行", ee.getMessage());
            return false;
        }
    }

    private void initComponent() {
        this.txt = (TextView) findViewById(C0326R.C0328id.txt);
        this.tv1 = (TextView) findViewById(C0326R.C0328id.tv1);
        this.tv2 = (TextView) findViewById(C0326R.C0328id.tv2);
        this.tv3 = (TextView) findViewById(C0326R.C0328id.tv3);
        this.tv4 = (TextView) findViewById(C0326R.C0328id.tv4);
        this.tv5 = (TextView) findViewById(C0326R.C0328id.tv5);
        this.llTab1 = (LinearLayout) findViewById(C0326R.C0328id.ll_tab1);
        this.llTab2 = (LinearLayout) findViewById(C0326R.C0328id.ll_tab2);
        this.llTab3 = (LinearLayout) findViewById(C0326R.C0328id.ll_tab3);
        this.llTab4 = (LinearLayout) findViewById(C0326R.C0328id.ll_tab4);
        this.llTab5 = (LinearLayout) findViewById(C0326R.C0328id.ll_tab5);
        this.llTab1.setOnClickListener(this);
        this.llTab2.setOnClickListener(this);
        this.llTab3.setOnClickListener(this);
        this.llTab4.setOnClickListener(this);
        this.llTab5.setOnClickListener(this);
    }

    private void inint() {
        String str = "";
        this.MsgStr = str;
        Intent intent = getIntent();
        try {
            userId = Integer.valueOf(Integer.parseInt(intent.getStringExtra("extra_user_id")));
            authToken = intent.getStringExtra("extra_auth_token");
            doctorName = intent.getStringExtra("extra_auth_name");
            employment = intent.getStringExtra("extra_auth_employment");
            if (userId.intValue() == 19661123) {
                this.bTestFlag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.service = new PreferencesService(this);
        Map<String, String> params = this.service.loadconfig();
        sampleTime = Integer.parseInt((String) params.get("SampleTime"));
        this.iNioseFilter = Integer.parseInt((String) params.get("NoiseFilter"));
        this.iBaselinedriftFilter = Integer.parseInt((String) params.get("BaselinedriftFilter"));
        this.iMyoelectricFilter = Integer.parseInt((String) params.get("MyoelectricFilter"));
        this.m_iLeadNum = Integer.parseInt((String) params.get("LeadNum"));
        this.tv_list = new ArrayList();
        this.tv_list.add(this.tv1);
        this.tv_list.add(this.tv2);
        this.tv_list.add(this.tv3);
        this.tv_list.add(this.tv4);
        this.tv_list.add(this.tv5);
        changePageSelect(0);
        this.iRecOneData = new short[18];
        this.listenStatus = Boolean.valueOf(false);
        this.SendStatus = false;
        this.bRecUDP = false;
        this.strPatientName = str;
        this.iPatientSex = 0;
        this.iPatientAge = 20;
        this.iRecDataNum = 0;
        this.bLeadStatus = new byte[21];
        this.iRecData = new short[21];
        this.iOld18LeadData = new short[14];
        this.iNew18LeadData = new short[14];
        for (int i = 0; i < 14; i++) {
            this.iOld18LeadData[i] = 2048;
        }
        this.drawSample = (DrawSample) findViewById(C0326R.C0328id.drawpanel);
        this.strPatientName = getPreferences(0).getString(USERNAME, str);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", 1).show();
            finish();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "请打开蓝牙！！！", 0).show();
            new Thread().start();
        }
        this.mBuffer = new ArrayList();
        this.dbHelper = new DBHelper(this, DB_NAME, null, 1);
        f39db = this.dbHelper.getWritableDatabase();
        BluetoochAddress = this.DeviceMapper.findDeviceMacAddr(f39db);
        StringBuilder sb = new StringBuilder();
        sb.append("获取到数据库地址");
        sb.append(BluetoochAddress);
        Log.d("获取到db中蓝牙地址", sb.toString());
        PrintStream printStream = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("=====>Addr:");
        sb2.append(BluetoochAddress);
        printStream.println(sb2.toString());
        String str2 = BluetoochAddress;
        if (str2 != null && !str2.equals(str)) {
            try {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("找到对应的蓝牙设备，开始连接。！");
                sb3.append(BluetoochAddress);
                Toast.makeText(this, sb3.toString(), 1).show();
                connectDevice(BluetoochAddress);
            } catch (Exception e2) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("未找到对应的蓝牙设备！");
                sb4.append(BluetoochAddress);
                Toast.makeText(this, sb4.toString(), 1).show();
            }
        }
        this.drawSample.SetDisplayMode(this.m_iLeadNum);
    }

    public boolean connectDevice(String BluetoochAddress2) {
        StringBuilder sb = new StringBuilder();
        sb.append("开始连接蓝牙设备");
        sb.append(BluetoochAddress2);
        Log.d(TAG, sb.toString());
        BluetoothDevice device = this.mBluetoothAdapter.getRemoteDevice(BluetoochAddress2);
        for (BluetoothDevice bondedDevice : this.mBluetoothAdapter.getBondedDevices()) {
            if (!device.getAddress().equals(bondedDevice.getAddress())) {
                try {
                    ClsUtils.removeBond(bondedDevice.getClass(), bondedDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return connect(device);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0326R.C0328id.ll_tab1 /*2131230842*/:
                setOnclickEvent(0);
                return;
            case C0326R.C0328id.ll_tab2 /*2131230843*/:
                setOnclickEvent(1);
                return;
            case C0326R.C0328id.ll_tab3 /*2131230844*/:
                setOnclickEvent(2);
                return;
            case C0326R.C0328id.ll_tab4 /*2131230845*/:
                setOnclickEvent(3);
                return;
            case C0326R.C0328id.ll_tab5 /*2131230846*/:
                setOnclickEvent(4);
                return;
            default:
                return;
        }
    }

    public void setOnclickEvent(int event) {
        Intent intent = new Intent();
        if (event == 0) {
            this.txt.setText("患者的触发事件");
            intent.setClass(this, PatientListActivity.class);
            Bundle bundle = new Bundle();
            String str = "Flag";
            if (this.strPatientName.length() > 0) {
                bundle.putInt(str, 1);
                bundle.putString("Name", this.strPatientName);
                bundle.putInt("Age", this.iPatientAge);
                bundle.putInt("Sex", this.iPatientSex);
            } else {
                bundle.putInt(str, 0);
            }
            intent.putExtras(bundle);
            startActivityForResult(intent, 2);
        } else if (event == 1) {
            this.txt.setText("波形复位的触发事件");
            BluetoothEcgCMD(53, 0, 0, 0);
            this.drawSample.m_iSampleNum = 0;
            for (int j = 0; j < 14; j++) {
                this.iOld18LeadData[j] = 2048;
            }
        } else if (event == 2) {
            this.txt.setText("波形存储的触发事件");
            if (diagnosingPatientPhone == null) {
                Toast.makeText(this, "需要选择一个患者", 1).show();
                return;
            }
            SendCMD(this.m_iLeadNum);
            DrawSample drawSample2 = this.drawSample;
            drawSample2.m_bSample = true;
            drawSample2.m_iSampleNum = 0;
            drawSample2.m_bSampleOver = false;
        } else if (event == 3) {
            this.txt.setText("导联选择的触发事件");
            this.EcgCmd = 21;
            if (this.drawSample.mLeadNum == 12) {
                this.m_iLeadNum = 15;
            } else if (this.drawSample.mLeadNum == 15) {
                this.m_iLeadNum = 18;
            } else if (this.drawSample.mLeadNum == 18) {
                this.m_iLeadNum = 12;
            }
            this.drawSample.SetDisplayMode(this.m_iLeadNum);
            this.drawSample.m_iSampleNum = 0;
            SendCMD(this.m_iLeadNum);
            this.service.saveconfig(sampleTime, this.iNioseFilter, this.iBaselinedriftFilter, this.iMyoelectricFilter, this.m_iLeadNum);
        } else if (event == 4) {
            this.txt.setText("病例上传的触发事件");
            intent.setClass(this, DataListActivity.class);
            Bundle bundle2 = new Bundle();
            bundle2.putInt("LEADNUM", this.m_iLeadNum);
            intent.putExtras(bundle2);
            startActivityForResult(intent, 0);
        }
        changePageSelect(event);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String str = TAG;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String oldStr = BluetoochAddress;
            if (resultCode == -1) {
                BluetoochAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                String BluetoochName = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_NAME);
                PrintStream printStream = System.out;
                StringBuilder sb = new StringBuilder();
                sb.append("回传蓝牙地址：");
                sb.append(BluetoochAddress);
                printStream.println(sb.toString());
                this.DeviceMapper.insertDeviceMacAddr(f39db, BluetoochName, BluetoochAddress);
                if (tag == 0) {
                    connectDevice(BluetoochAddress);
                } else if (!BluetoochAddress.equals(oldStr)) {
                    connectDevice(BluetoochAddress);
                }
            }
        } else if (requestCode == 2) {
            try {
                diagnosingPatientId = Integer.parseInt(data.getStringExtra("extra_patient_id"));
                StringBuilder sb2 = new StringBuilder();
                sb2.append("diagnosingPatientId=");
                sb2.append(diagnosingPatientId);
                Log.i(str, sb2.toString());
                diagnosingPatientName = data.getStringExtra("extra_patient_name");
                StringBuilder sb3 = new StringBuilder();
                sb3.append("diagnosingPatientName=");
                sb3.append(diagnosingPatientName);
                Log.i(str, sb3.toString());
                diagnosingPatientPhone = data.getStringExtra("extra_patient_phone");
                StringBuilder sb4 = new StringBuilder();
                sb4.append("diagnosingPatientPhone=");
                sb4.append(diagnosingPatientPhone);
                Log.i(str, sb4.toString());
                diagnosingPatientSex = data.getStringExtra("extra_patient_sex");
                StringBuilder sb5 = new StringBuilder();
                sb5.append("diagnosingPatientSex=");
                sb5.append(diagnosingPatientSex);
                Log.i(str, sb5.toString());
                diagnosingPatientBirthday = data.getStringExtra("extra_patient_birthday");
            } catch (Exception e) {
                e.printStackTrace();
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("患者：");
            String str2 = diagnosingPatientName;
            if (str2 == null) {
                str2 = "";
            }
            sb6.append(str2);
            setTitle(sb6.toString());
        }
    }

    public void changePageSelect(int index) {
        for (int i = 0; i < this.tv_list.size(); i++) {
            if (index == i) {
                ((TextView) this.tv_list.get(i)).setTextColor(getResources().getColor(C0326R.color.colorLightRed));
            } else {
                ((TextView) this.tv_list.get(i)).setTextColor(getResources().getColor(C0326R.color.colorTextGrey));
            }
        }
    }

    public void SendCMD(int iLeadNum) {
        byte iData5 = 3;
        byte iData6 = 1;
        if (iLeadNum == 12) {
            iData5 = 1;
            iData6 = 1;
        } else if (iLeadNum == 15) {
            iData5 = 2;
            iData6 = 1;
        } else if (iLeadNum == 18) {
            iData5 = 3;
            iData6 = 1;
        }
        this.drawSample.m_iSampleNum = 0;
        BluetoothEcgCMD(21, iData5, iData6, 0);
    }

    public int saveEcgData() {
        String filename;
        String str = TAG;
        String str2 = "";
        diagnosingFilename = str2;
        diagnosingTime = str2;
        short[] sArr = new short[18];
        byte[] buf = new byte[28];
        byte[] HeadBuf = new byte[256];
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = fmt.format(date);
        FileOutputStream fos = null;
        int i = this.m_iLeadNum;
        HeadBuf[0] = (byte) i;
        String str3 = "Ecg";
        if (i == 12) {
            StringBuilder sb = new StringBuilder();
            sb.append(str3);
            sb.append(time);
            sb.append(".w");
            filename = sb.toString();
        } else if (i == 15) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str3);
            sb2.append(time);
            sb2.append(".q");
            filename = sb2.toString();
        } else if (i != 18) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return -1;
        } else {
            try {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(time);
                sb3.append(".g");
                filename = sb3.toString();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        }
        FileOutputStream fos2 = openFileOutput(filename, 0);
        for (int i2 = 0; i2 < 28; i2++) {
            fos2.write(HeadBuf);
        }
        for (int i3 = 0; i3 < sampleTime; i3++) {
            for (int j = 0; j < 14; j++) {
                short tmp = this.drawSample.SampleData[i3][j];
                buf[(j * 2) + 1] = (byte) (tmp / 256);
                buf[j * 2] = (byte) tmp;
            }
            fos2.write(buf);
        }
        diagnosingFilename = filename;
        fmt.applyPattern("yyyy-MM-dd HH:mm:ss");
        diagnosingTime = fmt.format(new Date());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("diagnosingFilename:");
        sb4.append(diagnosingFilename);
        Log.d(str, sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append("diagnosingTime:");
        sb5.append(diagnosingTime);
        Log.d(str, sb5.toString());
        if (fos2 != null) {
            try {
                fos2.close();
            } catch (IOException e6) {
                e6.printStackTrace();
            }
        }
        return 0;
    }

    public int ifUpload() {
        Builder builder = new Builder(this);
        builder.setTitle("上传数据");
        builder.setItems(new String[]{"是", "否"}, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                System.out.println(which);
                if (which == 0) {
                    MainActivity.this.uploadData();
                }
            }
        });
        builder.create().show();
        return 0;
    }

    public void BluetoothEcgCMD(byte cmd, byte data5, byte data6, byte data7) {
        if (tag != 0) {
            byte[] OutBuf = new byte[22];
            for (short i = 0; i < 21; i = (short) (i + 1)) {
                OutBuf[i] = 0;
            }
            OutBuf[0] = 65;
            OutBuf[21] = 0;
            OutBuf[1] = 73;
            OutBuf[2] = 75;
            OutBuf[3] = 68;
            OutBuf[4] = cmd;
            OutBuf[5] = data5;
            OutBuf[6] = data6;
            OutBuf[7] = data7;
            for (short i2 = 0; i2 < 21; i2 = (short) (i2 + 1)) {
                OutBuf[21] = (byte) (OutBuf[21] + OutBuf[i2]);
            }
            this.mConnectedThread.write(OutBuf);
        }
    }

    public void WifiEcgCMD(byte cmd, byte data5, byte data6) {
        byte[] OutBuf = new byte[22];
        for (short i = 0; i < 21; i = (short) (i + 1)) {
            OutBuf[i] = 0;
        }
        OutBuf[21] = 0;
        OutBuf[0] = 65;
        OutBuf[1] = 73;
        OutBuf[2] = 75;
        OutBuf[3] = 68;
        OutBuf[4] = cmd;
        OutBuf[5] = data5;
        OutBuf[6] = data6;
        for (short i2 = 0; i2 < 21; i2 = (short) (i2 + 1)) {
            OutBuf[21] = (byte) (OutBuf[21] + OutBuf[i2]);
        }
        try {
            if (!this.SendStatus) {
                this.socketSend = new DatagramSocket(9000);
                this.SendStatus = true;
            }
            this.socketSend.send(new DatagramPacket(OutBuf, OutBuf.length, InetAddress.getByName("192.168.1.45"), 9000));
            this.socketSend.close();
            this.socketSend.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        getPreferences(0).edit().putString(USERNAME, this.strPatientName).commit();
        super.onStop();
    }

    public int ProcessDeviceData(byte[] inBuf, int iRecPackageNum) {
        byte[] bArr = new byte[1024];
        for (int i = 0; i < iRecPackageNum; i++) {
            if (inBuf[i] == -1) {
                if (this.iRecDataNum == 17) {
                    for (int k = 0; k < 3; k++) {
                        byte BitData = 64;
                        for (int j = 0; j < 7; j++) {
                            if ((this.iRecData[k] & BitData) == BitData) {
                                this.bLeadStatus[(k * 7) + j] = -1;
                            } else {
                                this.bLeadStatus[(k * 7) + j] = 1;
                            }
                            BitData = (byte) (BitData >> 1);
                        }
                    }
                    for (int j2 = 0; j2 < 14; j2++) {
                        this.iNew18LeadData[j2] = (short) (this.iOld18LeadData[j2] + (this.bLeadStatus[j2] * this.iRecData[j2 + 3]));
                    }
                    this.iRecStatus = (short) (this.iRecData[3] >> 3);
                    if (this.bLeadStatus[15] == -1 && this.iRecStatus == 12) {
                        for (int j3 = 0; j3 < 14; j3++) {
                            this.iOld18LeadData[j3] = 2048;
                        }
                    }
                    for (int j4 = 0; j4 < 12; j4++) {
                        this.iRecOneData[j4 + 6] = this.iNew18LeadData[j4 + 2];
                    }
                    short[] sArr = this.iRecOneData;
                    short[] sArr2 = this.iNew18LeadData;
                    sArr[1] = sArr2[0];
                    sArr[2] = sArr2[1];
                    sArr[0] = (short) ((sArr2[0] + 2048) - sArr2[1]);
                    sArr[3] = (short) ((((sArr2[1] - 2048) / 2) + 2048) - (sArr2[0] - 2048));
                    sArr[4] = (short) ((((sArr2[0] - 2048) / 2) + 2048) - (sArr2[1] - 2048));
                    sArr[5] = (short) ((sArr2[0] + sArr2[1]) / 2);
                    for (int j5 = 0; j5 < 18; j5++) {
                        this.drawSample.RecData[this.drawSample.iRecIndex][j5] = this.iRecOneData[j5];
                    }
                    if (this.drawSample.m_bSampleOver) {
                        this.drawSample.m_bSampleOver = false;
                        diagnosingId = saveEcgData();
                        this.bAskUpLoad = true;
                        invalidateOptionsMenu();
                    }
                    DrawSample drawSample2 = this.drawSample;
                    drawSample2.iRecIndex = (short) (drawSample2.iRecIndex + 1);
                    short s = this.drawSample.iRecIndex;
                    this.drawSample.getClass();
                    if (s == 2500) {
                        this.drawSample.iRecIndex = 0;
                    }
                    this.drawSample.iRecNum++;
                    this.drawSample.iRecTestNum++;
                    for (int j6 = 0; j6 < 14; j6++) {
                        this.iOld18LeadData[j6] = this.iNew18LeadData[j6];
                    }
                } else {
                    System.out.println("当前帧ERROR！");
                    this.drawSample.iErrTestNum++;
                }
                this.iRecDataNum = 0;
            } else {
                int i2 = this.iRecDataNum;
                if (i2 > 18) {
                    this.iRecDataNum = 0;
                    this.drawSample.iErrTestNum++;
                    Log.e(TAG, "出现错误帧");
                } else {
                    this.iRecData[i2] = (short) (inBuf[i] & 255);
                    this.iRecDataNum = i2 + 1;
                }
            }
        }
        return 1;
    }

    public void uploadData() {
        String str = TAG;
        Log.i(str, "UpLoadData");
        StringBuilder sb = new StringBuilder();
        sb.append("diagnosingFilename=");
        sb.append(diagnosingFilename);
        Log.i(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("diagnosingTime=");
        sb2.append(diagnosingTime);
        Log.i(str, sb2.toString());
        String base64str = Base64Util.byte2Base64String(readEcgDataByte(diagnosingFilename));
        Map<String, String> map = new HashMap<>();
        map.put("Name", diagnosingPatientName);
        map.put("Time", diagnosingTime);
        map.put("Sex", diagnosingPatientSex);
        String str2 = diagnosingPatientBirthday;
        String str3 = "Year";
        if (str2 == null || str2.length() <= 4) {
            map.put(str3, "1980");
        } else {
            map.put(str3, diagnosingPatientBirthday.substring(0, 4));
        }
        map.put("LeadSys", "Wilson");
        String str4 = "";
        map.put("Class", str4);
        map.put("Bed", str4);
        map.put("Filename", diagnosingFilename);
        map.put("SamUser", str4);
        Map result = null;
        try {
            result = this.systemController.upLoadDiagnose(diagnosingFilename, base64str, JsonUtil.mapToJson(map), md5(diagnosingFilename));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(result.get("code").toString()) > 0) {
            Toast.makeText(this, "完成数据上传", 1).show();
        } else {
            Toast.makeText(this, "数据上传失败", 1).show();
        }
    }

    public byte[] readEcgDataByte(String fileName) {
        FileInputStream fis = null;
        byte[] buffer = null;
        try {
            fis = openFileInput(fileName);
            int size = fis.available();
            buffer = new byte[size];
            int iReadSize = fis.read(buffer);
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("FileSize：");
            sb.append(size);
            sb.append("   ReadSize:");
            sb.append(iReadSize);
            printStream.println(sb.toString());
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fis != null) {
                fis.close();
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
        return buffer;
    }

    public String md5(String fileName) {
        int i;
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesDir().getPath().toString());
        sb.append(MqttTopic.TOPIC_LEVEL_SEPARATOR);
        sb.append(fileName);
        File file = new File(sb.toString());
        if (!file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte[] buffer = new byte[8192];
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            FileInputStream in2 = new FileInputStream(file);
            while (true) {
                int read = in2.read(buffer);
                int len = read;
                if (read == -1) {
                    break;
                }
                md5.update(buffer, 0, len);
            }
            for (byte b : md5.digest()) {
                String temp = Integer.toHexString(b & 255);
                if (temp.length() == 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("0");
                    sb2.append(temp);
                    temp = sb2.toString();
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(result);
                sb3.append(temp);
                result = sb3.toString();
            }
            try {
                in2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
        return result;
    }
}
