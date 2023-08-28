package com.bluetooth.cup.app;

import android.app.Application;
import android.content.Context;
import com.bluetooth.cup.manager.BleManager;
import com.bluetooth.cup.util.SharePreferenceUtil;
import com.orm.SugarContext;

public class BleCupApplication extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initViseBle();
        SharePreferenceUtil.init(mContext);
        SugarContext.init(this);
    }

    private void initViseBle() {
        BleManager.getInstance().init(mContext);
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}