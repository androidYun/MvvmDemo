package com.xhs.mlecg;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.Utils.MqttUtil;
import com.example.administrator.Utils.SPUtils;
import com.github.barteksc.pdfviewer.PDFView;
import java.io.File;
import java.io.FileInputStream;

public class Drawpdf extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    /* renamed from: p */
    private int f38p;
    private TextView pageTv;
    private TextView pageTv1;
    private PDFView pdfView;
    /* access modifiers changed from: private */
    public SharedPreferences sharedPreferences = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.activity_drawpdf);
        getPermission();
        this.pdfView = (PDFView) findViewById(C0326R.C0328id.pdfView);
        this.pageTv = (TextView) findViewById(C0326R.C0328id.pageTv);
        this.pageTv1 = (TextView) findViewById(C0326R.C0328id.pageTv1);
        int intValue = ((Integer) SPUtils.get(this, "page", Integer.valueOf(0))).intValue();
        PDFView pDFView = this.pdfView;
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesDir().getPath().toString());
        sb.append("/ecg.pdf");
        pDFView.fromFile(new File(sb.toString())).load();
    }

    private void getPermission() {
        String str = "android.permission.READ_EXTERNAL_STORAGE";
        if (ContextCompat.checkSelfPermission(this, str) != 0) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, str)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
            }
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 1);
        }
        do {
        } while (ContextCompat.checkSelfPermission(this, str) != 0);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        SPUtils.put(this, "page", Integer.valueOf(this.f38p));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu_print, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        String str = "";
        String str2 = "/mlecg/print/999";
        String str3 = "topic";
        String str4 = "1883";
        String str5 = "port";
        String str6 = "106.12.241.103";
        String str7 = "ip";
        String str8 = "printPreference";
        if (itemId == C0326R.C0328id.print) {
            this.sharedPreferences = getSharedPreferences(str8, 0);
            String ip = this.sharedPreferences.getString(str7, str6);
            String port = this.sharedPreferences.getString(str5, str4);
            String topic = this.sharedPreferences.getString(str3, str2);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(getFilesDir().getPath().toString());
                sb.append("/ecg.pdf");
                FileInputStream fis = new FileInputStream(new File(sb.toString()));
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(fis.available());
                Log.d("drawpdf", sb2.toString());
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                if (buffer.length > 0) {
                    MqttUtil.getInstance(this, ip, Integer.parseInt(port)).publish(topic, buffer);
                    Toast.makeText(this, "已发送到打印机", 1).show();
                } else {
                    Toast.makeText(this, "数据为空", 1).show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("page4", ex.getMessage());
            }
        } else if (itemId == C0326R.C0328id.print_setting) {
            Builder builder = new Builder(this);
            AlertDialog dialog = builder.create();
            View dialogView = View.inflate(this, C0326R.layout.activity_print_setting, null);
            dialog.setView(dialogView);
            dialog.show();
            EditText ipEditText = (EditText) dialogView.findViewById(C0326R.C0328id.print_ip);
            EditText portEditText = (EditText) dialogView.findViewById(C0326R.C0328id.print_port);
            EditText topicEditText = (EditText) dialogView.findViewById(C0326R.C0328id.print_topic);
            Button btn = (Button) dialogView.findViewById(C0326R.C0328id.print_ok);
            Builder builder2 = builder;
            this.sharedPreferences = getSharedPreferences(str8, 0);
            ipEditText.setText(this.sharedPreferences.getString(str7, str6));
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(this.sharedPreferences.getString(str5, str4));
            portEditText.setText(sb3.toString());
            topicEditText.setText(this.sharedPreferences.getString(str3, str2));
            final EditText editText = ipEditText;
            final EditText editText2 = portEditText;
            final EditText editText3 = topicEditText;
            final AlertDialog alertDialog = dialog;
            C03051 r1 = new OnClickListener() {
                public void onClick(View v) {
                    Drawpdf drawpdf = Drawpdf.this;
                    drawpdf.sharedPreferences = drawpdf.getSharedPreferences("printPreference", 0);
                    Editor editor = Drawpdf.this.sharedPreferences.edit();
                    editor.putString("ip", editText.getText().toString());
                    editor.putString("port", editText2.getText().toString());
                    editor.putString("topic", editText3.getText().toString());
                    editor.commit();
                    Toast.makeText(Drawpdf.this, "打印参数设置成功。", 1).show();
                    alertDialog.dismiss();
                }
            };
            btn.setOnClickListener(r1);
        }
        return super.onOptionsItemSelected(item);
    }
}
