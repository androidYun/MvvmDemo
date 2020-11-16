package com.xhs.mlecg;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttWireMessage;

public class page4 extends AppCompatActivity {
    String[] EcgScaleNames = {"5mm/mv", "10mm/mv", "20mm/mv"};
    String[] EcgSpeedNames = {"12.5mm/s", "25mm/s"};
    DrawEcg drawEcg;
    String fileName = null;
    private byte iLeadNum;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0326R.layout.activity_data_list);
        setTitle("数据显示");
        this.fileName = getIntent().getExtras().getString("fileName");
        this.drawEcg = new DrawEcg(this);
        int iReadPoint = ReadEcgData(this.fileName);
        if (iReadPoint == -1) {
            String str = "文件格式不对";
            Toast.makeText(this, str, 1).show();
            System.out.println(str);
            finish();
        }
        this.drawEcg.SetLeadNum(this.iLeadNum, iReadPoint);
        setContentView((View) this.drawEcg);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0326R.C0329menu.menu_ecg, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        int checkedItem = 0;
        int i = this.drawEcg.mSpeed;
        if (i == 1) {
            checkedItem = 1;
        } else if (i == 2) {
            checkedItem = 0;
        }
        MenuItem item = menu.getItem(0);
        StringBuilder sb = new StringBuilder();
        sb.append("走纸速度：");
        sb.append(this.EcgSpeedNames[checkedItem]);
        item.setTitle(sb.toString());
        int checkedItem2 = 0;
        int i2 = this.drawEcg.mScale;
        if (i2 == 1) {
            checkedItem2 = 0;
        } else if (i2 == 2) {
            checkedItem2 = 1;
        } else if (i2 == 4) {
            checkedItem2 = 2;
        }
        MenuItem item2 = menu.getItem(1);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("波形幅度：");
        sb2.append(this.EcgScaleNames[checkedItem2]);
        item2.setTitle(sb2.toString());
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String str = "确定";
        if (id == C0326R.C0328id.SetEcgScale) {
            Builder builder = new Builder(this);
            builder.setTitle("波形幅度");
            int checkedItem = 0;
            int i = this.drawEcg.mScale;
            if (i == 1) {
                checkedItem = 0;
            } else if (i == 2) {
                checkedItem = 1;
            } else if (i == 4) {
                checkedItem = 2;
            }
            String[] strArr = this.EcgScaleNames;
            builder.setSingleChoiceItems(new String[]{strArr[0], strArr[1], strArr[2]}, checkedItem, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        page4.this.drawEcg.mScale = 1;
                    } else if (which == 1) {
                        page4.this.drawEcg.mScale = 2;
                    } else if (which == 2) {
                        page4.this.drawEcg.mScale = 4;
                    }
                    page4.this.invalidateOptionsMenu();
                }
            });
            builder.setPositiveButton(str, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    page4.this.drawEcg.ReDraw();
                }
            });
            builder.create().show();
        }
        if (id == C0326R.C0328id.SetEcgSpeed) {
            Builder builder2 = new Builder(this);
            builder2.setTitle("走纸速度");
            int checkedItem2 = 0;
            int i2 = this.drawEcg.mSpeed;
            if (i2 == 1) {
                checkedItem2 = 1;
            } else if (i2 == 2) {
                checkedItem2 = 0;
            }
            String[] strArr2 = this.EcgSpeedNames;
            builder2.setSingleChoiceItems(new String[]{strArr2[0], strArr2[1]}, checkedItem2, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        page4.this.drawEcg.mSpeed = 2;
                    } else if (which == 1) {
                        page4.this.drawEcg.mSpeed = 1;
                    }
                    page4.this.invalidateOptionsMenu();
                }
            });
            builder2.setPositiveButton(str, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    page4.this.drawEcg.ReDraw();
                }
            });
            builder2.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    public int ReadEcgData(String fileName2) {
        short tmp;
        short[] sArr = new short[18];
        int i = 1;
        byte[] bArr = new byte[1];
        short III = 256;
        byte[] HeadBuf = new byte[256];
        FileInputStream fis = null;
        int i2 = 0;
        String info = fileName2;
        String Filesuffix = info.substring(info.length() - 1);
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("读取数据：");
        sb.append(Filesuffix);
        printStream.println(sb.toString());
        this.iLeadNum = 0;
        if (Filesuffix.equalsIgnoreCase("w")) {
            this.iLeadNum = MqttWireMessage.MESSAGE_TYPE_PINGREQ;
        }
        if (Filesuffix.equalsIgnoreCase("q")) {
            this.iLeadNum = 15;
        }
        if (Filesuffix.equalsIgnoreCase("g")) {
            this.iLeadNum = 18;
        }
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("导联数：");
        sb2.append(this.iLeadNum);
        printStream2.println(sb2.toString());
        int i3 = -1;
        if (this.iLeadNum == 0) {
            return -1;
        }
        try {
            FileInputStream fis2 = openFileInput(fileName2);
            for (int i4 = 0; i4 < 28; i4++) {
                fis2.read(HeadBuf);
            }
            byte[] buffer = new byte[28];
            PrintStream printStream3 = System.out;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("导联数1：");
            sb3.append(this.iLeadNum);
            printStream3.println(sb3.toString());
            i2 = 0;
            while (true) {
                int read = fis2.read(buffer);
                int size = read;
                if (read == i3) {
                    break;
                }
                PrintStream printStream4 = System.out;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("导联数2：");
                sb4.append(size);
                printStream4.println(sb4.toString());
                for (int j = 0; j < 14; j++) {
                    if (buffer[j * 2] < 0) {
                        tmp = (short) ((buffer[(j * 2) + i] * 256) + III + buffer[j * 2]);
                    } else {
                        tmp = (short) ((buffer[(j * 2) + i] * 256) + buffer[j * 2]);
                    }
                    this.drawEcg.FileData[i2][j + 4] = tmp;
                }
                short II = this.drawEcg.FileData[i2][4];
                short III2 = this.drawEcg.FileData[i2][5];
                this.drawEcg.FileData[i2][1] = this.drawEcg.FileData[i2][4];
                this.drawEcg.FileData[i2][2] = this.drawEcg.FileData[i2][5];
                this.drawEcg.FileData[i2][0] = (short) ((II + 2048) - III2);
                this.drawEcg.FileData[i2][3] = (short) ((((III2 - 2048) / 2) + 2048) - (II - 2048));
                this.drawEcg.FileData[i2][4] = (short) ((((II - 2048) / 2) + 2048) - (III2 - 2048));
                this.drawEcg.FileData[i2][5] = (short) ((II + III2) / 2);
                i2++;
                i = 1;
                III = 256;
                i3 = -1;
            }
            if (fis2 != null) {
                try {
                    fis2.close();
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
        } catch (Throwable th) {
            Throwable th2 = th;
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            throw th2;
        }
        PrintStream printStream5 = System.out;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("读取点数：");
        sb5.append(i2);
        printStream5.println(sb5.toString());
        return i2;
    }
}
