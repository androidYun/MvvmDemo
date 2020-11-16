package com.xhs.mlecg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.support.p000v4.internal.view.SupportMenu;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.administrator.Controller.SystemController;
import com.example.administrator.Utils.PreferencesService;
import com.example.administrator.Utils.ProgressDialogUtil;
import com.example.administrator.Utils.ToastUtil;
import java.util.Map;
import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = LoginActivity.class.getName();
    private final int REQUEST_ENABLE_BLUETOOTH = 100;
    private BluetoothAdapter bluetoothAdapter;
    private Button loginButton;
    /* access modifiers changed from: private */
    public PreferencesService preferencesService = null;
    private TextView promptTextView;
    /* access modifiers changed from: private */
    public SystemController systemController = new SystemController();
    /* access modifiers changed from: private */
    public EditText userNameEditText;
    /* access modifiers changed from: private */
    public EditText userPasswordEditText;

    private class LoginThread implements Runnable {
        private String password;
        ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this, "用户登录", "用户正在登录，请稍等......");
        private ProgressDialogUtil progressDialogUtil = ProgressDialogUtil.getInstance();
        private String username;

        public LoginThread(String username2, String password2) {
            String str = "";
            this.username = str;
            this.password = str;
            this.username = username2;
            this.password = password2;
        }

        public void run() {
            String str = "AuthToken";
            this.progressDialogUtil.showProgressDialog(this.progressDialog);
            if (this.username.length() == 0 || this.password.length() == 0) {
                this.progressDialogUtil.showToastMessage("用户名或密码不能为空");
                this.progressDialogUtil.closeProgressDialog();
                return;
            }
            try {
                String encodePassword = LoginActivity.this.systemController.getEncodePassword(this.password);
                String str2 = "网络不通或者网站有问题";
                if (encodePassword == null) {
                    this.progressDialogUtil.showToastMessage(str2);
                    this.progressDialogUtil.closeProgressDialog();
                    return;
                }
                Map result = LoginActivity.this.systemController.loginApp(this.username, encodePassword);
                if (result == null) {
                    this.progressDialogUtil.showToastMessage(str2);
                    this.progressDialogUtil.closeProgressDialog();
                    return;
                }
                if (Integer.parseInt(result.get("code").toString()) == 0) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Map innerResult = (Map) result.get("obj");
                    intent.putExtra("extra_user_id", innerResult.get("Id").toString());
                    intent.putExtra("extra_auth_token", innerResult.get(str).toString());
                    intent.putExtra("extra_auth_name", innerResult.get("Name").toString());
                    intent.putExtra("extra_auth_employment", innerResult.get("HealthClinicName").toString());
                    LoginActivity.this.preferencesService.savelogin(this.username, this.password, innerResult.get(str).toString());
                    this.progressDialogUtil.closeProgressDialog();
                    LoginActivity.this.startActivity(intent);
                } else {
                    this.progressDialogUtil.showToastMessage("用户名或密码错误");
                    this.progressDialogUtil.closeProgressDialog();
                }
            } catch (JSONException e) {
                this.progressDialogUtil.showToastMessage("登录信息解析异常");
                this.progressDialogUtil.closeProgressDialog();
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("登录");
        setContentView((int) C0326R.layout.activity_login);
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        initialize();
        checkBluetooth();
    }

    private void checkBluetooth() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothAdapter bluetoothAdapter2 = this.bluetoothAdapter;
        if (bluetoothAdapter2 == null) {
            ToastUtil.showToast(this, "不支持蓝牙设备");
            finish();
            return;
        }
        if (!bluetoothAdapter2.isEnabled()) {
            this.promptTextView.setText("请打开蓝牙。");
            Log.d(TAG, "打开蓝牙设备");
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 100);
        } else {
            this.promptTextView.setText("蓝牙已经打开。");
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            String str = "LoginActivity";
            if (resultCode == -1) {
                Log.i(str, "蓝牙打开成功！");
                this.promptTextView.setText("蓝牙打开成功。");
                return;
            }
            String str2 = "蓝牙打开失败！";
            Log.i(str, str2);
            this.promptTextView.setText(str2);
            this.promptTextView.setTextColor(SupportMenu.CATEGORY_MASK);
        }
    }

    public void initialize() {
        this.preferencesService = new PreferencesService(this);
        Map<String, String> params = this.preferencesService.loadlogin();
        this.userNameEditText = (EditText) findViewById(C0326R.C0328id.edt_user_name);
        this.userPasswordEditText = (EditText) findViewById(C0326R.C0328id.edt_user_password);
        this.promptTextView = (TextView) findViewById(C0326R.C0328id.txt_prompt);
        this.userNameEditText.setText((CharSequence) params.get("username"));
        this.userPasswordEditText.setText((CharSequence) params.get("password"));
        this.loginButton = (Button) findViewById(C0326R.C0328id.btn_user_login);
        this.loginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new Thread(new LoginThread(LoginActivity.this.userNameEditText.getText().toString(), LoginActivity.this.userPasswordEditText.getText().toString())).start();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu_login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == C0326R.C0328id.server_url) {
            final EditText editText = new EditText(this);
            editText.setText(this.preferencesService.getServerUrl());
            new AlertDialog.Builder(this).setTitle("服务器地址").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    GlobalConfig.getInstance().setUrl(editText.getText().toString());
                    LoginActivity.this.preferencesService.setServerUrl(editText.getText().toString());
                    ToastUtil.showToast(LoginActivity.this, "服务器地址设置成功");
                }
            }).setNegativeButton("取消", null).show();
        }
        return true;
    }
}
