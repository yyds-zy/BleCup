package com.bluetooth.cup.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;


public class BleManager {

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothManager mBluetoothManager = null;
    private BluetoothLeScanner mBluetoothLeScanner = null;

    private static Context mContext;
    private static BleManager instance;


    private BleManager() {

    }

    public static BleManager getInstance(){
        if (instance == null) {
            synchronized (BleManager.class) {
                if (instance == null) {
                    instance = new BleManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
    }

    /**
     * 开始扫描
     * @param callback
     */
    public void startScan(ScanCallback callback) {
        // 初始化蓝牙适配器
        if (mBluetoothManager == null || mBluetoothAdapter == null || mBluetoothLeScanner == null) {
            return;
        }

        // 打开蓝牙
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            mBluetoothLeScanner.startScan(callback);
        }
    }

    /**
     * 停止扫描
     * @param callback
     */
    public void stopScan(ScanCallback callback) {
        if (mBluetoothLeScanner == null) return;
        mBluetoothLeScanner.stopScan(callback);
    }
}
