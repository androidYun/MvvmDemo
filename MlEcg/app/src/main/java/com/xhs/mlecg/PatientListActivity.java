package com.xhs.mlecg;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.example.administrator.Controller.SystemController;
import com.example.administrator.Mapper.impl.PatientMapper;
import com.example.administrator.Model.Patient;
import com.example.administrator.Utils.ProgressDialogUtil;
import com.example.administrator.Utils.ProgressDialogUtil.ActionInterface;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.json.JSONException;

public class PatientListActivity extends AppCompatActivity implements ActionInterface {
    /* access modifiers changed from: private */
    public static String TAG = PatientListActivity.class.getName();
    /* access modifiers changed from: private */
    public static int pageIndex = 1;
    /* access modifiers changed from: private */
    public static int pageSize = 15;
    /* access modifiers changed from: private */
    public static int totalItems = 0;
    private ArrayAdapter<String> adapter1;
    /* access modifiers changed from: private */
    public RadioButton femaleButton;
    private SimpleAdapter listAdapter;
    /* access modifiers changed from: private */
    public ListView listView;
    private Button loadMoreButton;
    /* access modifiers changed from: private */
    public RadioButton maleButton;
    private RadioButton noGenderButton;
    private Patient patient = new Patient();
    /* access modifiers changed from: private */
    public ArrayList<Map<String, Object>> patientListData;
    /* access modifiers changed from: private */
    public Map<String, Object> patientListDataItem;
    private PatientMapper patientMapper = new PatientMapper();
    /* access modifiers changed from: private */
    public EditText patientNameEditText;
    private Button queryButton;
    SystemController systemController = new SystemController();

    private class LoadMoreThread implements Runnable {
        private String patientName;
        ProgressDialog progressDialog = ProgressDialog.show(PatientListActivity.this, "查询患者", "正在查询患者，请稍等......");
        private ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();
        private String sex;

        public LoadMoreThread(String patientName2, String sex2) {
            String str = "";
            this.patientName = str;
            this.sex = str;
            this.patientName = patientName2;
            this.sex = sex2;
        }

        public void run() {
            this.progressDialogUtil.showProgressDialog(this.progressDialog);
            PatientListActivity.access$408();
            try {
                Map<String, Object> map = PatientListActivity.this.systemController.findPatientList(this.patientName, this.sex, PatientListActivity.pageIndex, PatientListActivity.pageSize);
                PatientListActivity.totalItems = ((Integer) map.get("total")).intValue();
                List<Patient> patients = (List) map.get("patientList");
                for (int i = 0; i < patients.size(); i++) {
                    PatientListActivity.this.patientListDataItem = new HashMap();
                    PatientListActivity.this.patientListDataItem.put("_patient_id", ((Patient) patients.get(i)).getId());
                    PatientListActivity.this.patientListDataItem.put("_patient_name", ((Patient) patients.get(i)).getPatientName());
                    PatientListActivity.this.patientListDataItem.put("_patient_phone", ((Patient) patients.get(i)).getPhone());
                    PatientListActivity.this.patientListDataItem.put("_patient_idCard", ((Patient) patients.get(i)).getIdCard());
                    PatientListActivity.this.patientListDataItem.put("_patient_gender", ((Patient) patients.get(i)).getSex());
                    Map access$700 = PatientListActivity.this.patientListDataItem;
                    String str = "_patient_birthday";
                    String str2 = "";
                    if (((Patient) patients.get(i)).getBirthday() != null) {
                        if (((Patient) patients.get(i)).getBirthday().length() != 0) {
                            str2 = ((Patient) patients.get(i)).getBirthday().replace(" 00:00:00", str2);
                        }
                    }
                    access$700.put(str, str2);
                    PatientListActivity.this.patientListData.add(PatientListActivity.this.patientListDataItem);
                }
                this.progressDialogUtil.setActionInterface(PatientListActivity.this);
                this.progressDialogUtil.callbackAction();
            } catch (JSONException e) {
                Log.e(PatientListActivity.TAG, e.getMessage());
                e.printStackTrace();
            } catch (Throwable th) {
                this.progressDialogUtil.closeProgressDialog();
                throw th;
            }
            this.progressDialogUtil.closeProgressDialog();
        }
    }

    private class QueryThread implements Runnable {
        private String patientName;
        ProgressDialog progressDialog = ProgressDialog.show(PatientListActivity.this, "查询患者", "正在查询患者，请稍等......");
        private ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();
        private String sex;

        public QueryThread(String patientName2, String sex2) {
            String str = "";
            this.patientName = str;
            this.sex = str;
            this.patientName = patientName2;
            this.sex = sex2;
        }

        public void run() {
            this.progressDialogUtil.showProgressDialog(this.progressDialog);
            PatientListActivity.this.patientListData.clear();
            PatientListActivity.pageIndex = 1;
            try {
                Map<String, Object> map = PatientListActivity.this.systemController.findPatientList(this.patientName, this.sex, PatientListActivity.pageIndex, PatientListActivity.pageSize);
                PatientListActivity.totalItems = ((Integer) map.get("total")).intValue();
                List<Patient> patients = (List) map.get("patientList");
                for (int i = 0; i < patients.size(); i++) {
                    PatientListActivity.this.patientListDataItem = new HashMap();
                    PatientListActivity.this.patientListDataItem.put("_patient_id", ((Patient) patients.get(i)).getId());
                    PatientListActivity.this.patientListDataItem.put("_patient_name", ((Patient) patients.get(i)).getPatientName());
                    PatientListActivity.this.patientListDataItem.put("_patient_phone", ((Patient) patients.get(i)).getPhone());
                    PatientListActivity.this.patientListDataItem.put("_patient_idCard", ((Patient) patients.get(i)).getIdCard());
                    PatientListActivity.this.patientListDataItem.put("_patient_gender", ((Patient) patients.get(i)).getSex());
                    Map access$700 = PatientListActivity.this.patientListDataItem;
                    String str = "_patient_birthday";
                    String str2 = "";
                    if (((Patient) patients.get(i)).getBirthday() != null) {
                        if (((Patient) patients.get(i)).getBirthday().length() != 0) {
                            str2 = ((Patient) patients.get(i)).getBirthday().replace(" 00:00:00", str2);
                        }
                    }
                    access$700.put(str, str2);
                    PatientListActivity.this.patientListData.add(PatientListActivity.this.patientListDataItem);
                }
                this.progressDialogUtil.setActionInterface(PatientListActivity.this);
                this.progressDialogUtil.callbackAction();
            } catch (JSONException e) {
                Log.e(PatientListActivity.TAG, e.getMessage());
                e.printStackTrace();
            } catch (Throwable th) {
                this.progressDialogUtil.closeProgressDialog();
                throw th;
            }
            this.progressDialogUtil.closeProgressDialog();
        }
    }

    static /* synthetic */ int access$408() {
        int i = pageIndex;
        pageIndex = i + 1;
        return i;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.activity_patient_list);
        initialize();
    }

    public void initialize() {
        setTitle("患者信息");
        this.noGenderButton = (RadioButton) findViewById(C0326R.C0328id.btn_Sex0);
        this.maleButton = (RadioButton) findViewById(C0326R.C0328id.btn_Sex1);
        this.femaleButton = (RadioButton) findViewById(C0326R.C0328id.btn_Sex2);
        this.noGenderButton.setChecked(true);
        this.queryButton = (Button) findViewById(C0326R.C0328id.bt_query_patients);
        this.queryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String patientName = PatientListActivity.this.patientNameEditText.getText().toString();
                String patientGender = "";
                if (PatientListActivity.this.maleButton.isChecked()) {
                    patientGender = "男";
                } else if (PatientListActivity.this.femaleButton.isChecked()) {
                    patientGender = "女";
                }
                new Thread(new QueryThread(patientName, patientGender)).start();
            }
        });
        this.patientNameEditText = (EditText) findViewById(C0326R.C0328id.edt_patient_name);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getInt("Flag") == 1) {
            String s_value = bundle.getString("Name");
            int i = bundle.getInt("Age");
            this.patientNameEditText.setText(s_value);
            if (bundle.getInt("Sex") == 0) {
                this.maleButton.setChecked(true);
            } else {
                this.femaleButton.setChecked(true);
            }
        }
        this.listView = (ListView) findViewById(C0326R.C0328id.patient_listView);
        this.patientListData = new ArrayList<>();
        String str = "";
        new Thread(new QueryThread(str, str)).start();
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Map<String, Object> listItem = (Map) PatientListActivity.this.listView.getItemAtPosition(position);
                Builder builder = new Builder(PatientListActivity.this);
                builder.setItems(new String[]{"查看/编辑", "选中", "删除"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println(which);
                        String str = "1";
                        String str2 = "_patient_phone";
                        String str3 = "extra_patient_phone";
                        String str4 = "extra_patient_id";
                        String str5 = "_patient_id";
                        if (which == 0) {
                            Intent intent0 = new Intent(PatientListActivity.this, PatientEditActivity.class);
                            intent0.putExtra("extra_title_text", "编辑患者信息");
                            intent0.putExtra(str4, listItem.get(str5).toString());
                            intent0.putExtra(str3, listItem.get(str2).toString());
                            intent0.putExtra("extra_action_type", str);
                            PatientListActivity.this.startActivity(intent0);
                        } else if (which == 1) {
                            Intent intent1 = new Intent(PatientListActivity.this, MainActivity.class);
                            intent1.putExtra("Flag", str);
                            intent1.putExtra(str4, listItem.get(str5).toString());
                            intent1.putExtra("extra_patient_name", listItem.get("_patient_name").toString());
                            intent1.putExtra(str3, listItem.get(str2).toString());
                            intent1.putExtra("extra_patient_sex", listItem.get("_patient_gender").toString());
                            intent1.putExtra("extra_patient_idCard", listItem.get("_patient_idCard").toString());
                            intent1.putExtra("extra_patient_birthday", listItem.get("_patient_birthday").toString());
                            PatientListActivity.this.setResult(2, intent1);
                            PatientListActivity.this.finish();
                        } else if (which == 2) {
                            PrintStream printStream = System.out;
                            StringBuilder sb = new StringBuilder();
                            sb.append("删除");
                            sb.append(listItem.get(str5));
                            printStream.println(sb.toString());
                            if (PatientListActivity.this.systemController.delPatient(listItem.get(str5).toString())) {
                                Toast.makeText(PatientListActivity.this, "患者删除成功", 1).show();
                                String str6 = "";
                                new Thread(new QueryThread(str6, str6)).start();
                                return;
                            }
                            Toast.makeText(PatientListActivity.this, "患者删除失败", 1).show();
                        }
                    }
                });
                builder.create().show();
            }
        });
        this.loadMoreButton = (Button) findViewById(C0326R.C0328id.button_load_more);
        this.loadMoreButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String patientName = PatientListActivity.this.patientNameEditText.getText().toString();
                String patientGender = "";
                if (PatientListActivity.this.maleButton.isChecked()) {
                    patientGender = "男";
                } else if (PatientListActivity.this.femaleButton.isChecked()) {
                    patientGender = "女";
                }
                new Thread(new LoadMoreThread(patientName, patientGender)).start();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            String str = "";
            new Thread(new QueryThread(str, str)).start();
        }
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
        String str = "";
        new Thread(new QueryThread(str, str)).start();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            Intent intent1 = new Intent(this, MainActivity.class);
            intent1.putExtra("Flag", "0");
            setResult(2, intent1);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showList() {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, this.patientListData, C0326R.layout.listview_patient, new String[]{"_patient_id", "_patient_name", "_patient_phone", "_patient_gender", "_patient_birthday"}, new int[]{C0326R.C0328id.patient_id, C0326R.C0328id.patient_name, C0326R.C0328id.patient_phone, C0326R.C0328id.patient_gender, C0326R.C0328id.patient_birthday});
        this.listAdapter = simpleAdapter;
        this.listView.setAdapter(this.listAdapter);
        int size = this.patientListData.size();
        int i = totalItems;
        String str = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        String str2 = "";
        if (size == i) {
            Button button = this.loadMoreButton;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(this.patientListData.size());
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
        sb2.append(this.patientListData.size());
        sb2.append(str);
        sb2.append(totalItems);
        sb2.append("      加载更多......");
        button2.setText(sb2.toString());
        this.loadMoreButton.setEnabled(true);
    }

    public void action() {
        showList();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu_patient, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0326R.C0328id.add_patient) {
            Intent intent0 = new Intent(this, PatientEditActivity.class);
            intent0.putExtra("extra_title_text", "添加患者");
            intent0.putExtra("extra_action_type", "0");
            startActivity(intent0);
        }
        return super.onOptionsItemSelected(item);
    }
}
