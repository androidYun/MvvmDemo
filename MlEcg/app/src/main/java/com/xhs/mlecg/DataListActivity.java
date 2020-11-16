package com.xhs.mlecg;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.administrator.Controller.SystemController;
import com.example.administrator.Controller.userController;
import com.example.administrator.Model.Diagnose;
import com.example.administrator.Utils.Base64Util;
import com.example.administrator.Utils.ProgressDialogUtil;
import com.example.administrator.Utils.ProgressDialogUtil.ActionInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;

public class DataListActivity extends AppCompatActivity implements ActionInterface {
    /* access modifiers changed from: private */
    public static int pageIndex = 1;
    /* access modifiers changed from: private */
    public static int pageSize = 15;
    /* access modifiers changed from: private */
    public static int totalItems = 0;
    int bReportFlag;
    /* access modifiers changed from: private */
    public ArrayList<Map<String, Object>> diagnoseListData;
    /* access modifiers changed from: private */
    public Map<String, Object> diagnoseListDataItem;
    DrawEcg drawEcg;
    private SimpleAdapter listAdapter;
    /* access modifiers changed from: private */
    public ListView listView;
    private Button loadMoreButton;
    /* access modifiers changed from: private */
    public EditText patientNameEditText;
    /* access modifiers changed from: private */
    public byte[] pdfData;
    /* access modifiers changed from: private */
    public ProgressDialog progressDialog;
    private Button queryButton;
    String[] strDataOperate0;
    String[] strDataOperate1;
    String[] strDataOperate2;
    /* access modifiers changed from: private */
    public SystemController systemController = new SystemController();
    userController userController = new userController();

    private class LoadMoreThread implements Runnable {
        private String patientName = "";
        ProgressDialog progressDialog = ProgressDialog.show(DataListActivity.this, "查询数据", "正在查询数据，请稍等......");
        private ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();

        public LoadMoreThread(String patientName2) {
            this.patientName = patientName2;
        }

        public void run() {
            this.progressDialogUtil.showProgressDialog(this.progressDialog);
            try {
                DataListActivity.access$608();
                Map<String, Object> map = DataListActivity.this.systemController.findEcgList(this.patientName, DataListActivity.pageIndex, DataListActivity.pageSize);
                DataListActivity.totalItems = ((Integer) map.get("total")).intValue();
                List<Diagnose> diagnoses = (List) map.get("ecgList");
                for (int i = 0; i < diagnoses.size(); i++) {
                    DataListActivity.this.diagnoseListDataItem = new HashMap();
                    DataListActivity.this.diagnoseListDataItem.put("_diagnose_id", ((Diagnose) diagnoses.get(i)).getDiagnoseId());
                    DataListActivity.this.diagnoseListDataItem.put("_create_time", ((Diagnose) diagnoses.get(i)).getDiagnoseCreateTime());
                    DataListActivity.this.diagnoseListDataItem.put("_file_name", ((Diagnose) diagnoses.get(i)).getDiagnoseFileName());
                    DataListActivity.this.diagnoseListDataItem.put("_ecgdata_id", ((Diagnose) diagnoses.get(i)).getDiagnoseUploadState());
                    DataListActivity.this.diagnoseListDataItem.put("_patient_name", ((Diagnose) diagnoses.get(i)).getPatient().getPatientName());
                    DataListActivity.this.diagnoseListDataItem.put("_patient_identity_number", ((Diagnose) diagnoses.get(i)).getPatient().getIdCard());
                    String str = "_upload_state";
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseUploadState().intValue() == 0) {
                        DataListActivity.this.diagnoseListDataItem.put(str, "未上传");
                    } else {
                        DataListActivity.this.diagnoseListDataItem.put(str, "已上传");
                    }
                    String str2 = "已回报告";
                    String str3 = "_result_state";
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseResultState().intValue() == 0) {
                        DataListActivity.this.diagnoseListDataItem.put(str3, "未回报告");
                    } else {
                        DataListActivity.this.diagnoseListDataItem.put(str3, str2);
                    }
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseUploadState().intValue() > 0 && ((Diagnose) diagnoses.get(i)).getDiagnoseResultState().intValue() == 0) {
                        try {
                            if (Integer.parseInt(DataListActivity.this.systemController.CheckReportExist(String.valueOf(((Diagnose) diagnoses.get(i)).getDiagnoseUploadState())).get("code").toString()) == 0) {
                                DataListActivity.this.diagnoseListDataItem.put(str3, str2);
                                DataListActivity.this.userController.updateDiagnoseResult_stateByPatient_id(MainActivity.f39db, ((Diagnose) diagnoses.get(i)).getDiagnoseId().intValue(), 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DataListActivity.this.diagnoseListData.add(DataListActivity.this.diagnoseListDataItem);
                }
                this.progressDialogUtil.setActionInterface(DataListActivity.this);
                this.progressDialogUtil.callbackAction();
            } catch (Exception e2) {
                Log.e("DataListActivity", e2.getMessage());
                e2.printStackTrace();
            } catch (Throwable th) {
                this.progressDialogUtil.closeProgressDialog();
                throw th;
            }
            this.progressDialogUtil.closeProgressDialog();
        }
    }

    private class QueryThread implements Runnable {
        private String patientName = "";
        ProgressDialog progressDialog = ProgressDialog.show(DataListActivity.this, "查询数据", "正在查询数据，请稍等......");
        private ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();
        private int type = 0;

        public QueryThread(String patientName2, int type2) {
            this.patientName = patientName2;
            this.type = type2;
        }

        public void run() {
            this.progressDialogUtil.showProgressDialog(this.progressDialog);
            try {
                DataListActivity.this.diagnoseListData.clear();
                DataListActivity.pageIndex = 1;
                Map<String, Object> map = DataListActivity.this.systemController.findEcgList(this.patientName, DataListActivity.pageIndex, DataListActivity.pageSize);
                DataListActivity.totalItems = ((Integer) map.get("total")).intValue();
                List<Diagnose> diagnoses = (List) map.get("ecgList");
                for (int i = 0; i < diagnoses.size(); i++) {
                    DataListActivity.this.diagnoseListDataItem = new HashMap();
                    DataListActivity.this.diagnoseListDataItem.put("_diagnose_id", ((Diagnose) diagnoses.get(i)).getDiagnoseId());
                    DataListActivity.this.diagnoseListDataItem.put("_create_time", ((Diagnose) diagnoses.get(i)).getDiagnoseCreateTime());
                    DataListActivity.this.diagnoseListDataItem.put("_file_name", ((Diagnose) diagnoses.get(i)).getDiagnoseFileName());
                    DataListActivity.this.diagnoseListDataItem.put("_ecgdata_id", ((Diagnose) diagnoses.get(i)).getDiagnoseUploadState());
                    DataListActivity.this.diagnoseListDataItem.put("_patient_name", ((Diagnose) diagnoses.get(i)).getPatient().getPatientName());
                    DataListActivity.this.diagnoseListDataItem.put("_patient_identity_number", ((Diagnose) diagnoses.get(i)).getPatient().getIdCard());
                    String str = "_upload_state";
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseUploadState().intValue() == 0) {
                        DataListActivity.this.diagnoseListDataItem.put(str, "未上传");
                    } else {
                        DataListActivity.this.diagnoseListDataItem.put(str, "已上传");
                    }
                    String str2 = "已回报告";
                    String str3 = "_result_state";
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseResultState().intValue() == 0) {
                        DataListActivity.this.diagnoseListDataItem.put(str3, "未回报告");
                    } else {
                        DataListActivity.this.diagnoseListDataItem.put(str3, str2);
                    }
                    if (((Diagnose) diagnoses.get(i)).getDiagnoseUploadState().intValue() > 0 && ((Diagnose) diagnoses.get(i)).getDiagnoseResultState().intValue() == 0) {
                        try {
                            if (Integer.parseInt(DataListActivity.this.systemController.CheckReportExist(String.valueOf(((Diagnose) diagnoses.get(i)).getDiagnoseUploadState())).get("code").toString()) == 0) {
                                DataListActivity.this.diagnoseListDataItem.put(str3, str2);
                                DataListActivity.this.userController.updateDiagnoseResult_stateByPatient_id(MainActivity.f39db, ((Diagnose) diagnoses.get(i)).getDiagnoseId().intValue(), 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    DataListActivity.this.diagnoseListData.add(DataListActivity.this.diagnoseListDataItem);
                }
                this.progressDialogUtil.setActionInterface(DataListActivity.this);
                this.progressDialogUtil.callbackAction();
            } catch (Exception e2) {
                Log.e("DataListActivity", e2.getMessage());
                e2.printStackTrace();
            } catch (Throwable th) {
                this.progressDialogUtil.closeProgressDialog();
                throw th;
            }
            this.progressDialogUtil.closeProgressDialog();
        }
    }

    public DataListActivity() {
        String str = "打开";
        String str2 = "删除";
        this.strDataOperate0 = new String[]{str, str2};
        this.strDataOperate1 = new String[]{str, str2};
        this.strDataOperate2 = new String[]{str, str2, "查看报告"};
        this.bReportFlag = 0;
        this.progressDialog = null;
    }

    static /* synthetic */ int access$608() {
        int i = pageIndex;
        pageIndex = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.activity_data_list);
        setTitle("数据管理");
        init();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            String str = "";
            bundle.putString("Name", str);
            bundle.putString("Age", str);
            intent.putExtras(bundle);
            setResult(-1, intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init() {
        this.patientNameEditText = (EditText) findViewById(C0326R.C0328id.edit_patient_name);
        this.queryButton = (Button) findViewById(C0326R.C0328id.bt_query);
        this.queryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Thread(new QueryThread(DataListActivity.this.patientNameEditText.getText().toString(), 1)).start();
            }
        });
        this.loadMoreButton = (Button) findViewById(C0326R.C0328id.button_load_more);
        this.loadMoreButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Thread(new LoadMoreThread(DataListActivity.this.patientNameEditText.getText().toString())).start();
            }
        });
        this.listView = (ListView) findViewById(C0326R.C0328id.diagnose_listView);
        this.diagnoseListData = new ArrayList<>();
        new Thread(new QueryThread("", 1)).start();
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String[] strDataOperate;
                final Map<String, Object> listItem = (Map) DataListActivity.this.listView.getItemAtPosition(position);
                Builder builder = new Builder(DataListActivity.this);
                String uploadflag = listItem.get("_upload_state").toString();
                String reportflag = listItem.get("_result_state").toString();
                if (uploadflag.equals("未上传")) {
                    strDataOperate = DataListActivity.this.strDataOperate0;
                } else if (reportflag.equals("未回报告")) {
                    strDataOperate = DataListActivity.this.strDataOperate1;
                    DataListActivity.this.bReportFlag = 0;
                } else {
                    strDataOperate = DataListActivity.this.strDataOperate2;
                    DataListActivity.this.bReportFlag = 1;
                }
                builder.setTitle("数据操作");
                builder.setItems(strDataOperate, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        String str = "_file_name";
                        if (which == 0) {
                            Intent intent = new Intent();
                            intent.setClass(DataListActivity.this, page4.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("fileName", listItem.get(str).toString());
                            intent.putExtras(bundle);
                            DataListActivity.this.startActivityForResult(intent, 0);
                            PrintStream printStream = System.out;
                            StringBuilder sb = new StringBuilder();
                            sb.append("打开");
                            sb.append(listItem.get(str));
                            printStream.println(sb.toString());
                        } else if (which == 1) {
                            if (DataListActivity.this.systemController.delEcgData(listItem.get("_diagnose_id").toString())) {
                                DataListActivity.this.deleteECcgFile(listItem.get(str).toString());
                                new Thread(new QueryThread("", 1)).start();
                                Toast.makeText(DataListActivity.this, "心电数据删除成功。", 0).show();
                            } else {
                                Toast.makeText(DataListActivity.this, "心电数据删除失败！", 0).show();
                            }
                            PrintStream printStream2 = System.out;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("删除");
                            sb2.append(listItem.get(str));
                            printStream2.println(sb2.toString());
                        } else if (which == 2) {
                            if (DataListActivity.this.bReportFlag == 1) {
                                try {
                                    new Thread() {
                                        ProgressDialog progressDialog = ProgressDialog.show(DataListActivity.this, "下载报告", "正在下载报告，请稍等......");
                                        ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();

                                        public void run() {
                                            String str = "report";
                                            this.progressDialogUtil.showProgressDialog(this.progressDialog);
                                            try {
                                                Map result = DataListActivity.this.systemController.downLoadReport(listItem.get("_diagnose_id").toString());
                                                if (result.get(str) != null) {
                                                    byte[] reportData = Base64Util.base64String2Byte(result.get(str).toString());
                                                    DataListActivity.this.pdfData = reportData;
                                                    DataListActivity.this.SaveReportToPdf(reportData);
                                                    Intent intent = new Intent();
                                                    intent.setClass(DataListActivity.this, Drawpdf.class);
                                                    intent.putExtras(new Bundle());
                                                    DataListActivity.this.startActivityForResult(intent, 0);
                                                } else {
                                                    this.progressDialogUtil.showToastMessage("没有发现报告，请联系管理员查看PDF文件是否存在！");
                                                }
                                                this.progressDialog.cancel();
                                            } catch (JSONException e) {
                                                this.progressDialog.cancel();
                                                e.printStackTrace();
                                            } catch (Throwable th) {
                                                this.progressDialogUtil.closeProgressDialog();
                                                throw th;
                                            }
                                            this.progressDialogUtil.closeProgressDialog();
                                        }
                                    }.start();
                                } catch (Exception e) {
                                    DataListActivity.this.progressDialog.cancel();
                                    e.printStackTrace();
                                }
                            }
                        } else if (which == 3) {
                            try {
                                System.out.println(DataListActivity.this.systemController.CheckReportExist("13085"));
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    public void showList() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, this.diagnoseListData, C0326R.layout.listview_diagnose, new String[]{"_diagnose_id", "_create_time", "_file_name", "_ecgdata_id", "_upload_state", "_result_state", "_patient_name", "_patient_identity_number"}, new int[]{C0326R.C0328id.diagnose_id, C0326R.C0328id.create_time, C0326R.C0328id.file_name, C0326R.C0328id.ecgdata_id, C0326R.C0328id.upload_state, C0326R.C0328id.result_state, C0326R.C0328id.diagnose_patient_name, C0326R.C0328id.diagnose_patient_identity_number});
        this.listAdapter = simpleAdapter;
        this.listView.setAdapter(this.listAdapter);
        int size = this.diagnoseListData.size();
        int i = totalItems;
        String str = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        String str2 = "";
        if (size == i) {
            Button button = this.loadMoreButton;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(this.diagnoseListData.size());
            sb.append(str);
            sb.append(totalItems);
            sb.append("      没有更多数据了！");
            button.setText(sb.toString());
            this.loadMoreButton.setEnabled(false);
            return;
        }
        Button button2 = this.loadMoreButton;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(this.diagnoseListData.size());
        sb2.append(str);
        sb2.append(totalItems);
        sb2.append("       加载更多......");
        button2.setText(sb2.toString());
        this.loadMoreButton.setEnabled(true);
    }

    public void SaveReportToPdf(byte[] data) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("ecg.pdf", 0);
            fos.write(data);
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        }
    }

    public void saveFile(byte[] data) {
        FileOutputStream fos = null;
        if (!Environment.getExternalStorageState().equals("mounted")) {
            Toast.makeText(this, "请检查SD卡", 0).show();
            return;
        }
        File file = Environment.getExternalStorageDirectory();
        String str = "======SD卡根目录：";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(file.getCanonicalPath().toString());
            Log.d(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file.getCanonicalPath());
            sb2.append("/ecg.pdf");
            fos = new FileOutputStream(new File(sb2.toString()));
            String str2 = "ancdknjskj";
            fos.write(data);
            Toast.makeText(this, "保存成功", 0).show();
            try {
                fos.close();
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            if (fos != null) {
                fos.close();
                fos.flush();
            }
        } catch (Throwable th) {
            if (fos != null) {
                try {
                    fos.close();
                    fos.flush();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
            throw th;
        }
    }

    public int ReadEcgData(String fileName) {
        short tmp;
        short[] sArr = new short[18];
        FileInputStream fis = null;
        int i = 0;
        try {
            fis = openFileInput(fileName);
            byte[] buffer = new byte[36];
            while (true) {
                int read = fis.read(buffer);
                int size = read;
                if (read == -1) {
                    break;
                }
                for (int j = 0; j < 18; j++) {
                    if (buffer[(j * 2) + 1] < 0) {
                        tmp = (short) ((buffer[j * 2] * 256) + 256 + buffer[(j * 2) + 1]);
                    } else {
                        tmp = (short) ((buffer[j * 2] * 256) + buffer[(j * 2) + 1]);
                    }
                    this.drawEcg.FileData[i][j] = tmp;
                }
                i++;
            }
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
        return 0;
    }

    public int deleteECcgFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesDir().getPath().toString());
        String str = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        sb.append(str);
        sb.append(fileName);
        File file = new File(sb.toString());
        if (!file.exists()) {
            PrintStream printStream = System.out;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("删除文件失败:");
            sb2.append(fileName);
            sb2.append("不存在！");
            printStream.println(sb2.toString());
            return 0;
        }
        file.delete();
        PrintStream printStream2 = System.out;
        StringBuilder sb3 = new StringBuilder();
        sb3.append(getFilesDir().getPath().toString());
        sb3.append(str);
        sb3.append(fileName);
        sb3.append("已删除");
        printStream2.println(sb3.toString());
        return 0;
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

    public String Bytes2HexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & 255);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu_data, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0326R.C0328id.refresh_report) {
            new Thread(new QueryThread("", 0)).start();
        }
        return super.onOptionsItemSelected(item);
    }

    public void action() {
        showList();
    }
}
