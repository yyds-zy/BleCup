package com.bluetooth.cup.ui;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import com.bluetooth.cup.listener.BlePermissionListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity {

    private static String[] permissions = {"android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"
            , "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    private static int REQUEST_CODE = 10001;
    private static int BLE_CODE = 10008;
    private static int LOCATION_CODE = 10009;
    private BlePermissionListener blePermissionListener;
    public LocationManager locationManager;
    public BluetoothAdapter mAdapter;


    public void setBlePermissionListener(BlePermissionListener blePermissionListener) {
        this.blePermissionListener = blePermissionListener;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppTheme();
        setContentView(getLayoutId());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        setToolBar();
        init();
        permission();
        initData();
    }

    private void setAppTheme() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#3F3F3F"));
        }
    }

    protected void openBle() {
        if (!mAdapter.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable, BLE_CODE);
        }
    }

    protected void openLocation() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, LOCATION_CODE);
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    protected abstract void initData();

    protected void setToolBar() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void nagivateTo(Class cls) {
        startActivity(new Intent(this, cls));
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission() {
        List<String> noGrantedList = new ArrayList<>();
        for (String permission : permissions) {
            int checkSelfPermission = checkSelfPermission(permission);
            if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
            } else if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                noGrantedList.add(permission);
            }
        }

        if (!noGrantedList.isEmpty()) {
            //注意：里边可能包含不能请求的权限
            String[] permissions = new String[noGrantedList.size()];
            noGrantedList.toArray(permissions);
            requestPermissions(permissions, REQUEST_CODE);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Log.e("tag_权限申请同意", permission);
                } else if (grantResult == PackageManager.PERMISSION_DENIED) {

                    boolean b = shouldShowRequestPermissionRationale(permission);
                    if (b) {
                        Log.e("tag_权限申请拒绝——还可以申请", permission);
                    } else {
                        Log.e("tag_权限申请拒绝--不可以申请", permission);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLE_CODE && resultCode == RESULT_OK) {
            blePermissionListener.openSuccess();
        } else {
            blePermissionListener.openFail();
        }

        if (requestCode == LOCATION_CODE && resultCode == RESULT_OK) {
            blePermissionListener.openSuccess();
        }
    }


    protected void showLocationDialog() {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        normalDialog.setCancelable(false);
        normalDialog.setMessage("当前设备需要开启位置服务才能扫描周边蓝牙，是否去打开？");
        normalDialog.setPositiveButton("继续",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openLocation();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        normalDialog.show();
    }
}
