package com.xhs.mlecg;

import android.app.Activity;
import android.os.Bundle;
import android.support.p003v7.widget.Toolbar;

public class page2 extends Activity {
    private Toolbar toolbar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0326R.layout.page2layout);
        this.toolbar = (Toolbar) findViewById(C0326R.C0328id.toolbar);
        this.toolbar.setTitle((CharSequence) "设置信息");
    }
}
