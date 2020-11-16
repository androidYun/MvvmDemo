package com.xhs.mlecg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.p000v4.internal.view.SupportMenu;
import android.support.p000v4.view.InputDeviceCompat;
import android.support.p000v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import java.lang.reflect.Array;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class DrawSample extends View implements Runnable {
    private static final String TAG = DrawSample.class.getName();
    private int[] BufXL;
    public short[] Ch18stru;
    String[] EcgDlNames;
    String[] EcgDlNames15;
    String[] EcgScaleNames;
    String[] EcgSpeedNames;
    final int MAXLEADS = 18;
    final int MAXRECBUFFERS = 2500;
    private int OneStep;
    private int OnemmPoint;
    public short[][] RecData;
    public short[][] SampleData;
    public boolean bTestFlag;
    private double dDelay;
    public double dSpeed;
    private int iCountXL;
    public short iDrawIndex;
    public int iDrawNum;
    public int iErrTestNum;
    public int iPreError;
    public short iRecIndex;
    public int iRecNum;
    public int iRecTestNum;
    private int iXLValue;
    private int mDrawHeight;
    private int mDrawWidth;
    public int mLeadNum;
    public int[] mRectEcgB;
    public int[] mRectEcgL;
    public int[] mRectEcgR;
    public int[] mRectEcgT;
    public int mScale;
    private int mTouchDownX;
    private int mTouchDownY;
    public boolean m_bSample;
    public boolean m_bSampleOver;
    public int m_iBeginDrawX;
    private int[] m_iOldEcgY;
    public int m_iPosX;
    public int m_iPrevSec;
    public int m_iSampleNum;
    Bitmap memBitmap = null;
    Bitmap memBitmapBK = null;
    Canvas memCanvas = null;
    Canvas memCanvasBK = null;
    int screen_height;
    int screen_width;

    public DrawSample(Context context, AttributeSet attrs) {
        Class<short> cls = short.class;
        super(context, attrs);
        this.RecData = (short[][]) Array.newInstance(cls, new int[]{2500, 18});
        this.SampleData = (short[][]) Array.newInstance(cls, new int[]{90100, 14});
        String str = "I";
        String str2 = "II";
        String str3 = "III";
        String str4 = "aVR";
        String str5 = "aVL";
        this.EcgDlNames = new String[]{str, str2, str3, str4, str5, "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V3R", "V4R", "V5R"};
        this.EcgDlNames15 = new String[]{str, str2, str3, str4, str5, "aVF", "V1", "V2", "V3", "V4", "V5", "V6", "V3R", "V4R", "V5R"};
        this.EcgScaleNames = new String[]{"5mm/mv   ", "10mm/mv   ", "20mm/mv   "};
        this.EcgSpeedNames = new String[]{"12.5mm/s", "25mm/s", "50mm/s"};
        this.iPreError = 0;
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        this.screen_width = windowManager.getDefaultDisplay().getWidth();
        this.screen_height = windowManager.getDefaultDisplay().getHeight();
        int i = this.screen_height;
        int i2 = this.screen_width;
        if (i > i2) {
            this.memBitmap = Bitmap.createBitmap(i, i, Config.ARGB_8888);
            this.memCanvas = new Canvas(this.memBitmap);
            int i3 = this.screen_height;
            this.memBitmapBK = Bitmap.createBitmap(i3, i3, Config.ARGB_8888);
            this.memCanvasBK = new Canvas(this.memBitmapBK);
        } else {
            this.memBitmap = Bitmap.createBitmap(i2, i2, Config.ARGB_8888);
            this.memCanvas = new Canvas(this.memBitmap);
            int i4 = this.screen_width;
            this.memBitmapBK = Bitmap.createBitmap(i4, i4, Config.ARGB_8888);
            this.memCanvasBK = new Canvas(this.memBitmapBK);
        }
        this.mLeadNum = 18;
        this.mRectEcgL = new int[18];
        this.mRectEcgR = new int[18];
        this.mRectEcgT = new int[18];
        this.mRectEcgB = new int[18];
        this.OnemmPoint = 60;
        this.OneStep = this.OnemmPoint / 20;
        this.iRecIndex = 0;
        this.iDrawIndex = 0;
        this.iRecNum = 0;
        this.iDrawNum = 0;
        this.Ch18stru = new short[18];
        this.m_iSampleNum = 0;
        this.m_bSample = false;
        this.m_bSampleOver = false;
        this.m_iPrevSec = 0;
        this.dSpeed = 2.5d;
        this.dSpeed = 10.0d;
        this.dDelay = 0.0d;
        this.BufXL = new int[1100];
        this.iXLValue = 0;
        this.iCountXL = 0;
        this.m_iBeginDrawX = 1;
        this.m_iPosX = this.m_iBeginDrawX;
        this.m_iOldEcgY = new int[this.mLeadNum];
        this.mScale = 20;
        this.iRecTestNum = 0;
        this.iErrTestNum = 0;
        this.bTestFlag = false;
        new Thread(this).start();
    }

    public void run() {
        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(1);
                i++;
                if (this.iRecIndex != this.iDrawIndex) {
                    DrawEcg(this.memCanvas);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Paint mBitPaint = new Paint(1);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        canvas.drawBitmap(this.memBitmap, new Rect(0, 0, this.mDrawWidth, this.mDrawHeight + 250), new Rect(0, 0, this.mDrawWidth, this.mDrawHeight + 250), mBitPaint);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mDrawWidth = w;
        this.mDrawHeight = h - 250;
        SetDisplayMode(this.mLeadNum);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void SetDisplayMode(int iMode) {
        int i = iMode;
        int iCol = 4;
        this.mLeadNum = i;
        this.m_iPrevSec = 0;
        this.m_iSampleNum = 0;
        if (i == 12) {
            iCol = 4;
        } else if (i == 15) {
            iCol = 5;
        } else if (i == 18) {
            iCol = 6;
        }
        for (int i2 = 0; i2 < iCol; i2++) {
            for (int j = 0; j < 3; j++) {
                int[] iArr = this.mRectEcgL;
                int i3 = (i2 * 3) + j;
                int i4 = this.mDrawWidth;
                iArr[i3] = ((i4 / 3) * j) + 4;
                this.mRectEcgR[(i2 * 3) + j] = iArr[(i2 * 3) + j] + ((i4 - 24) / 3);
                int[] iArr2 = this.mRectEcgT;
                int i5 = (i2 * 3) + j;
                int i6 = this.mDrawHeight;
                iArr2[i5] = ((i6 / iCol) * i2) + 4 + 80;
                this.mRectEcgB[(i2 * 3) + j] = iArr2[(i2 * 3) + j] + ((i6 - 24) / iCol);
            }
        }
        for (int i7 = 0; i7 < this.mLeadNum; i7++) {
            this.m_iOldEcgY[i7] = (this.mRectEcgT[i7] + this.mRectEcgB[i7]) / 2;
        }
        Paint paint = new Paint();
        this.memCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
        this.memCanvasBK.drawColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3.0f);
        paint.setColor(Color.rgb(150, 150, 150));
        for (int i8 = 0; i8 < this.mLeadNum; i8++) {
            Paint paint2 = paint;
            this.memCanvas.drawRoundRect((float) this.mRectEcgL[i8], (float) this.mRectEcgT[i8], (float) this.mRectEcgR[i8], (float) this.mRectEcgB[i8], 16.0f, 16.0f, paint2);
            this.memCanvasBK.drawRoundRect((float) this.mRectEcgL[i8], (float) this.mRectEcgT[i8], (float) this.mRectEcgR[i8], (float) this.mRectEcgB[i8], 16.0f, 16.0f, paint2);
        }
        paint.setColor(Color.rgb(50, 50, 50));
        paint.setStrokeWidth(3.0f);
        for (int i9 = 0; i9 < this.mLeadNum; i9++) {
            int j2 = 1;
            while (true) {
                int i10 = this.mRectEcgR[i9];
                int[] iArr3 = this.mRectEcgL;
                int i11 = i10 - iArr3[i9];
                int i12 = this.OnemmPoint;
                if (j2 >= (i11 / i12) + 1) {
                    break;
                }
                this.memCanvas.drawLine((float) (iArr3[i9] + (j2 * i12)), (float) (this.mRectEcgT[i9] + 1), (float) (iArr3[i9] + (i12 * j2)), (float) (this.mRectEcgB[i9] - 1), paint);
                Canvas canvas = this.memCanvasBK;
                int[] iArr4 = this.mRectEcgL;
                int i13 = iArr4[i9];
                int i14 = this.OnemmPoint;
                canvas.drawLine((float) (i13 + (j2 * i14)), (float) (this.mRectEcgT[i9] + 1), (float) (iArr4[i9] + (i14 * j2)), (float) (this.mRectEcgB[i9] - 1), paint);
                j2++;
            }
            int j3 = 1;
            while (true) {
                int i15 = this.mRectEcgB[i9];
                int[] iArr5 = this.mRectEcgT;
                int i16 = i15 - iArr5[i9];
                int i17 = this.OnemmPoint;
                if (j3 >= (i16 / i17) + 1) {
                    break;
                }
                this.memCanvas.drawLine((float) (this.mRectEcgL[i9] + 1), (float) (iArr5[i9] + (i17 * j3)), (float) (this.mRectEcgR[i9] - 1), (float) (iArr5[i9] + (i17 * j3)), paint);
                Canvas canvas2 = this.memCanvasBK;
                float f = (float) (this.mRectEcgL[i9] + 1);
                int[] iArr6 = this.mRectEcgT;
                int i18 = iArr6[i9];
                int i19 = this.OnemmPoint;
                canvas2.drawLine(f, (float) (i18 + (i19 * j3)), (float) (this.mRectEcgR[i9] - 1), (float) (iArr6[i9] + (i19 * j3)), paint);
                j3++;
            }
        }
        paint.setStyle(Style.FILL);
        paint.setTextSize(36.0f);
        paint.setColor(Color.rgb(255, 255, 150));
        if (i == 15) {
            for (int i20 = 0; i20 < this.mLeadNum; i20++) {
                this.memCanvas.drawText(this.EcgDlNames15[i20], (float) (this.mRectEcgL[i20] + 12), (float) (this.mRectEcgT[i20] + 40), paint);
                this.memCanvasBK.drawText(this.EcgDlNames15[i20], (float) (this.mRectEcgL[i20] + 12), (float) (this.mRectEcgT[i20] + 40), paint);
            }
        } else {
            for (int i21 = 0; i21 < this.mLeadNum; i21++) {
                this.memCanvas.drawText(this.EcgDlNames[i21], (float) (this.mRectEcgL[i21] + 12), (float) (this.mRectEcgT[i21] + 40), paint);
                this.memCanvasBK.drawText(this.EcgDlNames[i21], (float) (this.mRectEcgL[i21] + 12), (float) (this.mRectEcgT[i21] + 40), paint);
            }
        }
        DrawMessage(this.memCanvas);
        postInvalidate();
    }

    public void DrawMessage(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(48.0f);
        paint.setStyle(Style.FILL);
        canvas.drawRect(300.0f, 20.0f, 834.0f, 64.0f, paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setColor(-16711936);
        int i = 0;
        int j = 2;
        if (this.mScale == 20) {
            i = 1;
        }
        if (this.mScale == 40) {
            i = 2;
        }
        int i2 = i;
        if (this.dSpeed == 5.0d) {
            j = 1;
        }
        if (this.dSpeed == 10.0d) {
            j = 0;
        }
        int j2 = j;
        StringBuilder sb = new StringBuilder();
        sb.append(this.EcgScaleNames[i2]);
        sb.append(this.EcgSpeedNames[j2]);
        canvas.drawText(sb.toString(), 308.0f, 58.0f, paint);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        int i3 = this.mDrawWidth;
        float f = (float) (i3 - 20);
        canvas.drawRect((float) ((i3 - 184) - 20), 20.0f, f, 62.0f, paint);
        paint.setColor(-16711936);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Integer.toString(this.mLeadNum));
        sb2.append("导联");
        canvas.drawText(sb2.toString(), (float) ((this.mDrawWidth - 182) - 20), 60.0f, paint);
        postInvalidate();
    }

    public void Draw1msFlag(Canvas canvas) {
        if (this.mScale != 40) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(2.0f);
            paint.setColor(SupportMenu.CATEGORY_MASK);
            Path path = new Path();
            for (int i = 0; i < this.mLeadNum; i++) {
                path.moveTo(15.0f, (float) ((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2) + ((this.mScale * 30) / 10)));
                path.lineTo(15.0f, (float) (((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2)) - ((this.mScale * 30) / 10)));
                canvas.drawPath(path, paint);
                path.moveTo(10.0f, (float) ((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2) + ((this.mScale * 30) / 10)));
                path.lineTo(20.0f, (float) ((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2) + ((this.mScale * 30) / 10)));
                canvas.drawPath(path, paint);
                path.moveTo(10.0f, (float) (((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2)) - ((this.mScale * 30) / 10)));
                path.lineTo(20.0f, (float) (((this.mRectEcgT[i] / 2) + (this.mRectEcgB[i] / 2)) - ((this.mScale * 30) / 10)));
                canvas.drawPath(path, paint);
            }
            postInvalidate();
        }
    }

    public void DrawEcg(Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            if (this.iRecIndex != this.iDrawIndex) {
                DrawEcgOne(canvas);
            }
        }
    }

    public void DrawEcgOne(Canvas canvas) {
        boolean z;
        String str;
        int iValue;
        int iValue2;
        Canvas canvas2 = canvas;
        int i = 0;
        while (i < this.mLeadNum) {
            this.Ch18stru[i] = this.RecData[this.iDrawIndex][i];
            i++;
        }
        this.iDrawIndex = (short) (this.iDrawIndex + 1);
        if (this.iDrawIndex == 2500) {
            this.iDrawIndex = 0;
        }
        this.iDrawNum++;
        if (this.m_bSample) {
            int i2 = 0;
            while (i2 < 12) {
                this.SampleData[this.m_iSampleNum][i2 + 2] = this.Ch18stru[i2 + 6];
                i2++;
            }
            short[][] sArr = this.SampleData;
            int i3 = this.m_iSampleNum;
            short[] sArr2 = sArr[i3];
            short[] sArr3 = this.Ch18stru;
            sArr2[0] = sArr3[1];
            sArr[i3][1] = sArr3[2];
            this.m_iSampleNum = i3 + 1;
            if (this.m_iSampleNum == MainActivity.sampleTime) {
                this.m_bSample = false;
                this.m_bSampleOver = true;
                z = true;
                int i4 = i2;
            } else {
                z = false;
                int i5 = i2;
            }
        } else {
            z = false;
            int i6 = i;
        }
        short iValue3 = this.Ch18stru[1];
        int iXL = FindHR(iValue3);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(2.0f);
        if (iXL > 0) {
            paint.setStyle(Style.FILL);
            paint.setTextSize(48.0f);
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawRect(168.0f, 20.0f, 280.0f, 60.0f, paint);
            paint.setColor(SupportMenu.CATEGORY_MASK);
            canvas2.drawText(Integer.toString(iXL), 170.0f, 59.0f, paint);
        }
        this.dDelay += 1.0d;
        if (!z) {
            double d = this.dDelay;
            double d2 = this.dSpeed;
            if (d >= d2) {
                this.dDelay = d - d2;
            } else {
                return;
            }
        }
        Paint mBitPaint = new Paint(1);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        int[] iArr = this.mRectEcgL;
        int i7 = iArr[0];
        int i8 = this.m_iPosX;
        Rect mSrcRect = new Rect(i7 + i8, this.mRectEcgT[0], iArr[0] + i8 + this.OneStep, this.mDrawHeight + 250);
        int[] iArr2 = this.mRectEcgL;
        int i9 = iArr2[0];
        int i10 = this.m_iPosX;
        Rect mDestRect = new Rect(i9 + i10, this.mRectEcgT[0], iArr2[0] + i10 + this.OneStep, this.mDrawHeight + 250);
        for (int i11 = 0; i11 < 3; i11++) {
            int[] iArr3 = this.mRectEcgL;
            int i12 = iArr3[i11];
            int i13 = this.m_iPosX;
            mSrcRect.left = i12 + i13;
            int i14 = iArr3[i11] + i13;
            int i15 = this.OneStep;
            mSrcRect.right = i14 + i15 + 1;
            mDestRect.left = iArr3[i11] + i13;
            mDestRect.right = iArr3[i11] + i13 + i15 + 1;
            this.memCanvas.drawBitmap(this.memBitmapBK, mSrcRect, mDestRect, mBitPaint);
        }
        if (this.m_iPosX == this.m_iBeginDrawX) {
            for (int i16 = 0; i16 < this.mLeadNum; i16++) {
                int iValue4 = ((this.Ch18stru[i16] - 2048) * this.mScale) / 80;
                int[] iArr4 = this.mRectEcgT;
                int iValue5 = ((iArr4[i16] + this.mRectEcgB[i16]) / 2) - iValue4;
                if (iValue5 <= iArr4[i16] + 11) {
                    iValue5 = iArr4[i16] + 11;
                }
                int[] iArr5 = this.mRectEcgB;
                if (iValue5 >= iArr5[i16] - 11) {
                    iValue2 = iArr5[i16] - 11;
                } else {
                    iValue2 = iValue5;
                }
                this.m_iOldEcgY[i16] = iValue2;
            }
            this.m_iPosX += this.OneStep;
            return;
        }
        if (this.m_bSample) {
            paint.setColor(-16711936);
        } else {
            paint.setColor(InputDeviceCompat.SOURCE_ANY);
        }
        Path path = new Path();
        int i17 = iValue3;
        int i18 = i17;
        for (int i19 = 0; i19 < this.mLeadNum; i19++) {
            int iValue6 = ((this.Ch18stru[i19] - 2048) * this.mScale) / 40;
            StringBuilder sb = new StringBuilder();
            sb.append("value");
            sb.append(iValue6);
            Log.d("ch18", sb.toString());
            int[] iArr6 = this.mRectEcgT;
            int iValue7 = ((iArr6[i19] + this.mRectEcgB[i19]) / 2) - iValue6;
            if (iValue7 <= iArr6[i19] + 11) {
                iValue7 = iArr6[i19] + 11;
            }
            int[] iArr7 = this.mRectEcgB;
            if (iValue7 >= iArr7[i19] - 11) {
                iValue = iArr7[i19] - 11;
            } else {
                iValue = iValue7;
            }
            path.moveTo((float) (this.m_iPosX + this.mRectEcgL[i19]), (float) this.m_iOldEcgY[i19]);
            path.lineTo((float) (this.m_iPosX + this.mRectEcgL[i19] + this.OneStep), (float) iValue);
            this.m_iOldEcgY[i19] = iValue;
            canvas2.drawPath(path, paint);
        }
        paint.setStyle(Style.FILL);
        paint.setTextSize(48.0f);
        canvas2.drawText("心率：", 20.0f, 60.0f, paint);
        int iValue8 = this.m_iSampleNum / 500;
        int iValue9 = this.m_iPrevSec;
        String str2 = MqttTopic.TOPIC_LEVEL_SEPARATOR;
        if (iValue9 != iValue8) {
            this.m_iPrevSec = iValue8;
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            int i20 = this.mDrawWidth;
            str = str2;
            int iValue10 = iValue8;
            float f = (float) ((i20 - 20) + 10);
            Path path2 = path;
            Paint paint2 = mBitPaint;
            canvas.drawRect((float) ((i20 - 184) - 20), 20.0f, f, 65.0f, paint);
            paint.setColor(SupportMenu.CATEGORY_MASK);
            if (iValue10 < 10) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Integer.toString(this.m_iPrevSec));
                sb2.append(str);
                sb2.append(MainActivity.sampleTime / 500);
                sb2.append("秒");
                canvas2.drawText(sb2.toString(), (float) (((this.mDrawWidth - 182) - 20) + 28), 60.0f, paint);
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(Integer.toString(this.m_iPrevSec));
                sb3.append(str);
                sb3.append(MainActivity.sampleTime / 500);
                sb3.append("秒");
                canvas2.drawText(sb3.toString(), (float) ((this.mDrawWidth - 182) - 20), 60.0f, paint);
            }
        } else {
            str = str2;
            int i21 = iValue8;
            Path path3 = path;
            Paint paint3 = mBitPaint;
        }
        if (z) {
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            int i22 = this.mDrawWidth;
            float f2 = (float) (i22 - 20);
            Rect rect = mSrcRect;
            canvas.drawRect((float) ((i22 - 184) - 20), 20.0f, f2, 65.0f, paint);
            paint.setColor(SupportMenu.CATEGORY_MASK);
            canvas2.drawText("采集完成", (float) ((this.mDrawWidth - 182) - 20), 60.0f, paint);
        }
        if (this.bTestFlag && (this.iErrTestNum != this.iPreError || this.iRecTestNum % 100 == 0)) {
            this.iPreError = this.iErrTestNum;
            paint.setColor(ViewCompat.MEASURED_STATE_MASK);
            canvas.drawRect(300.0f, 20.0f, 834.0f, 64.0f, paint);
            paint.setColor(-16711936);
            StringBuilder sb4 = new StringBuilder();
            sb4.append(Integer.toString(this.iErrTestNum));
            sb4.append(str);
            sb4.append(Integer.toString(this.iRecTestNum));
            canvas2.drawText(sb4.toString(), 308.0f, 58.0f, paint);
        }
        int i23 = this.m_iPosX;
        int i24 = this.OneStep;
        this.m_iPosX = i23 + i24;
        if (this.m_iPosX >= (this.mRectEcgR[0] - i24) - 4) {
            this.m_iPosX = this.m_iBeginDrawX;
        }
        postInvalidate();
    }

    public int FindHR(int data) {
        int MaxDat = 0;
        for (int i = 10; i < 1000; i++) {
            int[] iArr = this.BufXL;
            int y1 = iArr[i] - iArr[i - 10];
            if (y1 > 0 && MaxDat < y1) {
                MaxDat = y1;
            }
        }
        if (MaxDat < 30) {
            MaxDat = 30;
        }
        this.iXLValue++;
        if (this.iXLValue > 999) {
            this.iXLValue = 0;
        }
        int[] iArr2 = this.BufXL;
        int i2 = this.iXLValue;
        iArr2[i2] = data;
        int i3 = i2 - 10;
        if (i3 < 0) {
            i3 += 1000;
        }
        int[] iArr3 = this.BufXL;
        if (iArr3[this.iXLValue] - iArr3[i3] > MaxDat / 2) {
            int i4 = this.iCountXL;
            if (i4 >= 98 && i4 <= 1050) {
                int i5 = 30000 / i4;
                this.iCountXL = 0;
                return i5;
            }
        }
        this.iCountXL++;
        if (this.iCountXL > 1000) {
            this.iCountXL = 0;
        }
        return 0;
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int action = event.getAction();
        if (action == 0) {
            this.mTouchDownX = (int) x;
            this.mTouchDownY = (int) y;
        } else if (action == 1) {
            if (((float) this.mTouchDownX) - x > 120.0f) {
                double d = this.dSpeed;
                if (d < 9.0d) {
                    this.dSpeed = d * 2.0d;
                }
            }
            if (x - ((float) this.mTouchDownX) > 120.0f) {
                double d2 = this.dSpeed;
                if (d2 > 3.0d) {
                    this.dSpeed = d2 / 2.0d;
                }
            }
            if (((float) this.mTouchDownY) - y > 120.0f) {
                int i = this.mScale;
                if (i < 40) {
                    this.mScale = i * 2;
                }
            }
            if (y - ((float) this.mTouchDownY) > 120.0f) {
                int i2 = this.mScale;
                if (i2 > 10) {
                    this.mScale = i2 / 2;
                }
            }
            Draw1msFlag(this.memCanvas);
            Log.i(TAG, "into drawMessage");
            DrawMessage(this.memCanvas);
        } else if (action != 2) {
        }
        return true;
    }
}
