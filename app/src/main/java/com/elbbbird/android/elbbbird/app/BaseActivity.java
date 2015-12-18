package com.elbbbird.android.elbbbird.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Base Activity
 * Created by zhanghailong-ms on 2015/12/18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 初始化layout
     */
    public abstract void setContentView();

    /**
     * 初始化数据和空间
     */
    public abstract void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();
        ButterKnife.bind(this);
        init();
    }
}
