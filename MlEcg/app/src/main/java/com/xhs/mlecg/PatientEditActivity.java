package com.xhs.mlecg;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.example.administrator.Controller.SystemController;
import com.example.administrator.Controller.userController;
import com.example.administrator.Model.Patient;

public class PatientEditActivity extends Activity {
    /* access modifiers changed from: private */
    public String actionType;
    /* access modifiers changed from: private */
    public RadioButton femaleButton;
    /* access modifiers changed from: private */
    public RadioButton maleButton;
    /* access modifiers changed from: private */
    public EditText patienIdentityNumberEditText;
    private Patient patient;
    /* access modifiers changed from: private */
    public DatePicker patientBirthdayEditText;
    /* access modifiers changed from: private */
    public String patientId;
    /* access modifiers changed from: private */
    public EditText patientIllDescEditText;
    /* access modifiers changed from: private */
    public EditText patientNameEditText;
    /* access modifiers changed from: private */
    public EditText patientPhoneNumberEditText;
    private Button resetButton;
    private Button saveButton;
    /* access modifiers changed from: private */
    public SystemController systemController = new SystemController();
    private Toolbar toolbar;
    userController userController = new userController();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0326R.layout.activity_patient_edit);
        initialize();
    }

    public void initialize() {
        String birthday;
        String str = "-";
        Intent intent = getIntent();
        String titleStr = intent.getStringExtra("extra_title_text");
        this.patientId = intent.getStringExtra("extra_patient_id");
        this.actionType = intent.getStringExtra("extra_action_type");
        this.toolbar = (Toolbar) findViewById(C0326R.C0328id.toolbar);
        this.toolbar.setTitle((CharSequence) titleStr);
        this.patientNameEditText = (EditText) findViewById(C0326R.C0328id.edt_patient_name);
        this.maleButton = (RadioButton) findViewById(C0326R.C0328id.btn_Sex1);
        this.femaleButton = (RadioButton) findViewById(C0326R.C0328id.btn_Sex2);
        this.patienIdentityNumberEditText = (EditText) findViewById(C0326R.C0328id.edit_identity_number);
        this.patientPhoneNumberEditText = (EditText) findViewById(C0326R.C0328id.edit_phone_number);
        this.patientIllDescEditText = (EditText) findViewById(C0326R.C0328id.edt_ill_desc);
        this.patientBirthdayEditText = (DatePicker) findViewById(C0326R.C0328id.dp_birthday);
        if (this.actionType.equals("1")) {
            this.patient = this.systemController.getPatient(this.patientId);
            this.patientNameEditText.setText(this.patient.getPatientName());
            this.patienIdentityNumberEditText.setText(this.patient.getIdCard());
            this.patientPhoneNumberEditText.setText(this.patient.getPhone());
            this.patientIllDescEditText.setText(this.patient.getIllDesc());
            if (this.patient.getSex().equals("男")) {
                this.maleButton.setChecked(true);
            } else {
                this.femaleButton.setChecked(true);
            }
            try {
                if (this.patient.getBirthday() != null) {
                    if (this.patient.getBirthday().length() != 0) {
                        birthday = this.patient.getBirthday().replace(" 00:00:00", "");
                        int year = Integer.parseInt(birthday.split(str)[0]);
                        int month = Integer.parseInt(birthday.split(str)[1]);
                        this.patientBirthdayEditText.init(year, month - 1, Integer.parseInt(birthday.split(str)[2]), null);
                    }
                }
                birthday = "1980-01-01";
                int year2 = Integer.parseInt(birthday.split(str)[0]);
                int month2 = Integer.parseInt(birthday.split(str)[1]);
                this.patientBirthdayEditText.init(year2, month2 - 1, Integer.parseInt(birthday.split(str)[2]), null);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.patientBirthdayEditText.init(1980, 0, 1, null);
            }
        }
        this.resetButton = (Button) findViewById(C0326R.C0328id.btn_reset_patient_info);
        this.resetButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String str = "";
                PatientEditActivity.this.patientNameEditText.setText(str);
                PatientEditActivity.this.patienIdentityNumberEditText.setText(str);
                PatientEditActivity.this.patientPhoneNumberEditText.setText(str);
                PatientEditActivity.this.patientIllDescEditText.setText(str);
                PatientEditActivity.this.maleButton.setChecked(true);
                PatientEditActivity.this.femaleButton.setChecked(false);
            }
        });
        this.saveButton = (Button) findViewById(C0326R.C0328id.btn_save_patient_info);
        this.saveButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String patientGender;
                Patient patient0;
                String patientName = PatientEditActivity.this.patientNameEditText.getText().toString();
                String patientNewIdentityNumber = PatientEditActivity.this.patienIdentityNumberEditText.getText().toString();
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(PatientEditActivity.this.patientBirthdayEditText.getYear());
                String str = "-";
                sb.append(str);
                sb.append(PatientEditActivity.this.patientBirthdayEditText.getMonth() + 1);
                sb.append(str);
                sb.append(PatientEditActivity.this.patientBirthdayEditText.getDayOfMonth());
                String patientBirthday = sb.toString();
                String patientPhoneNumber = PatientEditActivity.this.patientPhoneNumberEditText.getText().toString();
                String patientIllDesc = PatientEditActivity.this.patientIllDescEditText.getText().toString();
                String str2 = "";
                if (PatientEditActivity.this.maleButton.isChecked()) {
                    patientGender = "男";
                } else {
                    patientGender = "女";
                }
                String str3 = "0";
                if (PatientEditActivity.this.actionType.equals(str3)) {
                    patient0 = new Patient(Integer.valueOf(-1), patientName, patientNewIdentityNumber, patientGender, patientPhoneNumber, patientIllDesc);
                    patient0.setBirthday(patientBirthday);
                } else {
                    patient0 = new Patient(Integer.valueOf(Integer.parseInt(PatientEditActivity.this.patientId)), patientName, patientNewIdentityNumber, patientGender, patientPhoneNumber, patientIllDesc);
                    patient0.setBirthday(patientBirthday);
                }
                if (PatientEditActivity.this.inputCheckValue(patient0) == 1) {
                    Builder builder = new Builder(PatientEditActivity.this);
                    builder.setTitle("⚠️警告️");
                    builder.setMessage("\n请将个人信息填写完整！");
                    builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();
                    return;
                }
                if (PatientEditActivity.this.actionType.equals(str3)) {
                    if (PatientEditActivity.this.systemController.addPatient(patient0)) {
                        Toast.makeText(PatientEditActivity.this, "患者添加成功", 1).show();
                        PatientEditActivity.this.finish();
                    } else {
                        Toast.makeText(PatientEditActivity.this, "患者添加失败", 1).show();
                    }
                } else if (PatientEditActivity.this.systemController.editPatient(patient0)) {
                    Toast.makeText(PatientEditActivity.this, "患者编辑成功", 1).show();
                    PatientEditActivity.this.finish();
                } else {
                    Toast.makeText(PatientEditActivity.this, "患者编辑失败", 1).show();
                }
            }
        });
    }

    public int inputCheckValue(Patient patient2) {
        if (patient2.getPatientName().length() == 0) {
            return 1;
        }
        return 0;
    }
}
