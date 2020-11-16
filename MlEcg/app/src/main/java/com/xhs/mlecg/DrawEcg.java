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
import android.support.p000v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import java.io.PrintStream;
import java.lang.reflect.Array;

public class DrawEcg extends View {
    private static final int HEIGHT = 5500;
    private static final int STRIDE = 64;
    private static int WIDTH = 4700;
    String[] EcgDlNames;
    String[] EcgDlNames15;
    String[] EcgSpeedNames;
    public short[][] FileData = ((short[][]) Array.newInstance(short.class, new int[]{90100, 18}));
    private double dLenStart;
    private double dZoom;
    private int iBeginDataPoint = 0;
    private Canvas mCanvas;
    public int mLeadInterval;
    public int mLeadNum;
    public int mLradOffset;
    private int mMoveX;
    private int mMoveY;
    public int mScale;
    public int mSpeed;
    private int mSrcL;
    private int mSrcT;
    private int mTouchDownX;
    private int mTouchDownY;
    private int m_iRamHeight;
    private Bitmap myBitbmp;
    int screen_height;
    int screen_width;

    public DrawEcg(Context context) {
        super(context);
        String str = "I";
        String str2 = "II";
        String str3 = "III";
        String str4 = "aVR";
        String str5 = "aVL";
        String str6 = "aVF";
        String str7 = "V1";
        this.EcgDlNames = new String[]{str, str2, str3, str4, str5, str6, str7, "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V3R", "V4R", "V5R"};
        this.EcgDlNames15 = new String[]{str, str2, str3, str4, str5, str6, str7, "V2", "V3", "V4", "V5", "V6", "V4R", "V8", "V9"};
        this.EcgSpeedNames = new String[]{"12.5mm/s", "25mm/s"};
        this.dLenStart = 0.0d;
        WindowManager windowManager = (WindowManager) getContext().getSystemService("window");
        this.screen_width = windowManager.getDefaultDisplay().getWidth();
        this.screen_height = windowManager.getDefaultDisplay().getHeight();
        this.myBitbmp = Bitmap.createBitmap(this.screen_width * 3, this.screen_height, Config.ARGB_8888);
        this.mCanvas = new Canvas(this.myBitbmp);
        this.mCanvas.drawColor(-1);
        new Paint();
        this.mLeadNum = 18;
        this.mLeadInterval = this.screen_height / this.mLeadNum;
        this.mLradOffset = 100;
        this.mSrcL = 0;
        this.mSrcT = 0;
        this.mMoveX = 0;
        this.mMoveY = 0;
        this.dZoom = 1.0d;
        this.mScale = 1;
        this.mSpeed = 1;
    }

    public void SetLeadNum(int iLeadnum, int iReadPoint) {
        if (iLeadnum == 12) {
            this.m_iRamHeight = 3700;
        } else if (iLeadnum == 15) {
            this.m_iRamHeight = 4600;
        } else if (iLeadnum == 18) {
            this.m_iRamHeight = HEIGHT;
        }
        this.mLeadNum = iLeadnum;
        WIDTH = iReadPoint;
        this.iBeginDataPoint = 0;
        this.mLeadInterval = (this.screen_height - 250) / this.mLeadNum;
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("采样点数");
        sb.append(WIDTH);
        printStream.println(sb.toString());
        DrawEcg(this.mCanvas, this.iBeginDataPoint);
    }

    public void ReDraw() {
        PrintStream printStream = System.out;
        StringBuilder sb = new StringBuilder();
        sb.append("走纸速度");
        sb.append(this.mSpeed);
        printStream.println(sb.toString());
        if (this.iBeginDataPoint > WIDTH - (getWidth() * this.mSpeed)) {
            this.iBeginDataPoint = WIDTH - (getWidth() * this.mSpeed);
        }
        DrawEcg(this.mCanvas, this.iBeginDataPoint);
    }

    public void DrawEcg(Canvas canvas, int BeginPoint) {
        Canvas canvas2 = canvas;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(-3355444);
        paint.setColor(Color.rgb(243, 243, 243));
        canvas.drawRect(0.0f, 0.0f, (float) WIDTH, (float) this.screen_height, paint);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(Color.rgb(255, 223, 224));
        for (int i = 0; i < this.screen_width * 3; i += 20) {
            canvas.drawLine((float) i, 1.0f, (float) i, (float) this.screen_height, paint);
        }
        for (int j = 0; j < this.screen_height; j += 20) {
            canvas.drawLine(1.0f, (float) j, (float) (this.screen_width * 3), (float) j, paint);
        }
        paint.setColor(Color.rgb(235, 203, 204));
        for (int i2 = 0; i2 < this.screen_width * 3; i2 += 100) {
            canvas.drawLine((float) i2, 1.0f, (float) i2, (float) this.screen_height, paint);
        }
        for (int j2 = 0; j2 < this.screen_height; j2 += 100) {
            canvas.drawLine(1.0f, (float) j2, (float) (this.screen_width * 3), (float) j2, paint);
        }
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        int i3 = 0;
        while (true) {
            int i4 = this.screen_width;
            if (i3 >= (i4 * 3) - 1) {
                break;
            }
            int i5 = this.mSpeed;
            if ((i3 * i5) + BeginPoint >= i4 * i5 && (i3 * i5) + BeginPoint <= (WIDTH + (i4 * i5)) - 6) {
                for (int j3 = 0; j3 < this.mLeadNum; j3++) {
                    float f = (float) i3;
                    int i6 = this.mLeadInterval;
                    int i7 = i6 * j3;
                    int i8 = this.mLradOffset;
                    int i9 = i7 + i8;
                    short[][] sArr = this.FileData;
                    int i10 = this.mSpeed;
                    int i11 = BeginPoint + (i3 * i10);
                    int i12 = this.screen_width;
                    int i13 = ((sArr[i11 - (i12 * i10)][j3] - 2048) * 5) / 12;
                    int i14 = this.mScale;
                    canvas.drawLine(f, (float) (i9 - (i13 * i14)), (float) (i3 + 1), (float) (((i6 * j3) + i8) - ((((sArr[((BeginPoint + (i3 * i10)) + i10) - (i12 * i10)][j3] - 2048) * 5) / 12) * i14)), paint);
                }
            }
            i3++;
        }
        if (this.mScale == 1) {
            Path path = new Path();
            for (int i15 = 0; i15 < this.mLeadNum; i15++) {
                path.moveTo((float) (this.screen_width + 10), (float) ((this.mLeadInterval * i15) + this.mLradOffset));
                path.lineTo((float) (this.screen_width + 10 + 6), (float) ((this.mLeadInterval * i15) + this.mLradOffset));
                path.lineTo((float) (this.screen_width + 10 + 6), (float) (((this.mLeadInterval * i15) + this.mLradOffset) - 100));
                path.lineTo((float) (this.screen_width + 10 + 26), (float) (((this.mLeadInterval * i15) + this.mLradOffset) - 100));
                path.lineTo((float) (this.screen_width + 10 + 26), (float) ((this.mLeadInterval * i15) + this.mLradOffset));
                path.lineTo((float) (this.screen_width + 10 + 32), (float) ((this.mLeadInterval * i15) + this.mLradOffset));
                canvas2.drawPath(path, paint);
            }
        }
        paint.setTextSize(36.0f);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setStyle(Style.FILL);
        int i16 = 0;
        while (true) {
            int i17 = this.screen_width;
            if (i16 >= (i17 * 3) - 1) {
                break;
            }
            int i18 = this.mSpeed;
            if ((i16 * i18) + BeginPoint >= i17 * i18 && (i16 * i18) + BeginPoint <= (WIDTH + (i17 * i18)) - 6) {
                int iTmp = (BeginPoint + (i16 * i18)) - (i17 * i18);
                if (iTmp % 500 == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Integer.toString(iTmp / 500));
                    sb.append("s");
                    canvas2.drawText(sb.toString(), (float) (i16 + 4), 60.0f, paint);
                }
            }
            i16++;
        }
        for (int i19 = 0; i19 < this.mLeadNum; i19++) {
            canvas2.drawText(this.EcgDlNames[i19], (float) (i19 + 34 + this.screen_width), (float) ((this.mLeadInterval * i19) + this.mLradOffset), paint);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(Integer.toString(this.mScale * 5));
        sb2.append("mm/mv    ");
        sb2.append(this.EcgSpeedNames[2 - this.mSpeed]);
        canvas2.drawText(sb2.toString(), (float) (this.screen_width + 204), (float) (this.screen_height - 220), paint);
        postInvalidate();
    }

    public void DrawBK(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        paint.setColor(-3355444);
        paint.setColor(Color.rgb(243, 243, 243));
        canvas.drawRect(0.0f, 0.0f, (float) WIDTH, (float) this.m_iRamHeight, paint);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(1.0f);
        paint.setColor(Color.rgb(255, 223, 224));
        for (int i = 0; i < WIDTH; i += 20) {
            canvas.drawLine((float) i, 1.0f, (float) i, (float) this.m_iRamHeight, paint);
        }
        for (int j = 0; j < this.m_iRamHeight; j += 20) {
            canvas.drawLine(1.0f, (float) j, (float) WIDTH, (float) j, paint);
        }
        paint.setColor(Color.rgb(240, 143, 143));
        for (int i2 = 0; i2 < WIDTH; i2 += 100) {
            canvas.drawLine((float) i2, 1.0f, (float) i2, (float) this.m_iRamHeight, paint);
        }
        for (int j2 = 0; j2 < this.m_iRamHeight; j2 += 100) {
            canvas.drawLine(1.0f, (float) j2, (float) WIDTH, (float) j2, paint);
        }
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        for (int i3 = 0; i3 < WIDTH - 1; i3++) {
            for (int j3 = 0; j3 < this.mLeadNum; j3++) {
                float f = (float) i3;
                int i4 = (j3 * 300) + 200;
                short[][] sArr = this.FileData;
                canvas.drawLine(f, (float) (i4 - (((sArr[i3][j3] - 2048) * 5) / 12)), (float) (i3 + 1), (float) (((j3 * 300) + 200) - (((sArr[i3 + 1][j3] - 2048) * 5) / 12)), paint);
            }
        }
        paint.setTextSize(36.0f);
        paint.setColor(ViewCompat.MEASURED_STATE_MASK);
        paint.setStyle(Style.FILL);
        for (int i5 = 0; i5 < 20; i5++) {
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(i5));
            sb.append("s");
            canvas.drawText(sb.toString(), (float) ((i5 * 500) + 4), 60.0f, paint);
        }
        for (int i6 = 0; i6 < this.mLeadNum; i6++) {
            canvas.drawText(this.EcgDlNames[i6], (float) (i6 + 4), (float) ((i6 * 300) + 200), paint);
        }
        canvas.drawText("5mm/mv    25mm/s", 204.0f, (float) (this.m_iRamHeight - 50), paint);
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int iWidth = getWidth();
        int iHeight = getHeight();
        double d = (double) iWidth;
        double d2 = this.dZoom;
        Double.isNaN(d);
        double dWidth = d / d2;
        double d3 = (double) iHeight;
        Double.isNaN(d3);
        double dHeight = d3 / d2;
        Paint mBitPaint = new Paint(1);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        int i = (int) dWidth;
        int i2 = this.mSrcL;
        int i3 = i + i2;
        int i4 = this.mSrcT;
        canvas.drawBitmap(this.myBitbmp, new Rect(i3, i4, i2 + ((int) (2.0d * dWidth)), ((int) dHeight) + i4), new Rect(0, 0, getWidth(), getHeight()), mBitPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int i;
        MotionEvent motionEvent = event;
        float x = event.getX();
        float y = event.getY();
        int nCnt = event.getPointerCount();
        int action = event.getAction();
        double dSize = (double) event.getSize();
        if (nCnt != 2) {
        } else if (action == 5) {
            int xlen = Math.abs(((int) motionEvent.getX(0)) - ((int) motionEvent.getX(1)));
            int ylen = Math.abs(((int) motionEvent.getY(0)) - ((int) motionEvent.getY(1)));
            double d = (double) xlen;
            double d2 = (double) xlen;
            Double.isNaN(d);
            Double.isNaN(d2);
            double d3 = d * d2;
            double d4 = (double) ylen;
            double d5 = dSize;
            double dSize2 = (double) ylen;
            Double.isNaN(d4);
            Double.isNaN(dSize2);
            this.dLenStart = Math.sqrt(d3 + (d4 * dSize2));
        } else if (action != 6) {
            double d6 = dSize;
        } else {
            int ixlen = Math.abs(((int) motionEvent.getX(0)) - ((int) motionEvent.getX(1)));
            int iylen = Math.abs(((int) motionEvent.getY(0)) - ((int) motionEvent.getY(1)));
            double d7 = (double) ixlen;
            double d8 = (double) ixlen;
            Double.isNaN(d7);
            Double.isNaN(d8);
            double d9 = d7 * d8;
            double d10 = (double) iylen;
            int i2 = ixlen;
            double d11 = (double) iylen;
            Double.isNaN(d10);
            Double.isNaN(d11);
            double sqrt = Math.sqrt(d9 + (d10 * d11));
            double d12 = this.dLenStart;
            double d13 = this.dZoom;
            double d14 = dSize;
        }
        if (nCnt == 1) {
            if (action == 0) {
                this.mTouchDownX = (int) x;
                this.mTouchDownY = (int) y;
            } else if (action == 1) {
                this.mMoveX += (int) (((float) this.mTouchDownX) - x);
                if (this.mMoveX > (WIDTH - getWidth()) * this.mSpeed) {
                    this.mMoveX = WIDTH - (getWidth() * this.mSpeed);
                }
                if (this.mMoveX < 0) {
                    i = 0;
                    this.mMoveX = 0;
                } else {
                    i = 0;
                }
                this.mSrcL = this.mMoveX;
                this.mSrcL = i;
                this.iBeginDataPoint += ((int) (((float) this.mTouchDownX) - x)) * this.mSpeed;
                if (this.iBeginDataPoint < 0) {
                    this.iBeginDataPoint = 0;
                }
                if (this.iBeginDataPoint > WIDTH - (getWidth() * this.mSpeed)) {
                    this.iBeginDataPoint = WIDTH - (getWidth() * this.mSpeed);
                }
                DrawEcg(this.mCanvas, this.iBeginDataPoint);
            } else if (action == 2) {
                this.mSrcL = (int) (((float) this.mTouchDownX) - x);
                if (this.mSrcL > WIDTH - getWidth()) {
                    this.mSrcL = WIDTH - getWidth();
                }
            }
        }
        invalidate();
        return true;
    }
}
