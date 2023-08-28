package com.bluetooth.cup.ui.activity;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bluetooth.cup.R;
import com.bluetooth.cup.adapter.CommonListAdapter;
import com.bluetooth.cup.adapter.CommonViewHolder;
import com.bluetooth.cup.listener.BlePermissionListener;
import com.bluetooth.cup.manager.BleManager;
import com.bluetooth.cup.manager.BluetoothGattManager;
import com.bluetooth.cup.model.DeviceBean;
import com.bluetooth.cup.ui.BaseActivity;
import com.bluetooth.cup.util.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class BleScanActivity extends BaseActivity implements BlePermissionListener {

    RecyclerView mRecyclerView;
    CommonListAdapter<DeviceBean> adapter;
    List<DeviceBean> mDeviceList = new ArrayList<>();
    private ScanCallback scanCallback;
    private ProgressDialog dialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ble_scan;
    }

    @Override
    protected void init() {
        ImageView mToolbarIcon = findViewById(R.id.toolbar_icon);
        mToolbarIcon.setImageResource(R.mipmap.back);
        mToolbarIcon.setVisibility(View.VISIBLE);
        mToolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("可连接的水杯");
        mRecyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CommonListAdapter<DeviceBean>(this,mDeviceList,R.layout.layout_scan_ble) {

            @Override
            public void diffConvert(CommonViewHolder holder, DeviceBean deviceBean, String whereDirty) {

            }

            @Override
            public void convert(CommonViewHolder holder, final DeviceBean deviceBean) {
                holder.setText(R.id.ble_name,deviceBean.getDeviceName());
                holder.setText(R.id.connect_tv,"未连接");
                holder.getView(R.id.connect_tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (deviceBean.getDeviceName().contains("CUP") || deviceBean.getDeviceName().contains("C00")) {
                            BluetoothGattManager.getInstance().connectGatt(BleScanActivity.this, deviceBean.getDevice(), BluetoothGattManager.getInstance().getBluetoothGattCallback());
                            dialog = new ProgressDialog(BleScanActivity.this);
                            dialog.setMessage("正在连接中");
                            dialog.show();
                        } else {
                            Toast.makeText(BleScanActivity.this,"此设备不可连接",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        mRecyclerView.setAdapter(adapter);
        setBlePermissionListener(this);

        openBle();

        if (mAdapter.isEnabled() && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationDialog();
        }
    }

    @Override
    protected void initData() {
        BluetoothGattManager.getInstance().setGattCallback(gattCallBack);
        mDeviceList.clear();
        scanDevice();
        BleManager.getInstance().startScan(scanCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && 0 != BluetoothGattManager.getInstance().getDeviceState()) {
            if (scanCallback == null) return;
            BleManager.getInstance().startScan(scanCallback);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        BleManager.getInstance().stopScan(scanCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void openSuccess() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showLocationDialog();
        } else {
            BleManager.getInstance().startScan(scanCallback);
        }
    }

    @Override
    public void openFail() {
        finish();
    }

    BluetoothGattManager.GattCallBack gattCallBack = new BluetoothGattManager.GattCallBack() {
        @Override
        public void onMtuChanged(final BluetoothGatt gatt) {
            BleManager.getInstance().stopScan(scanCallback);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null) {
                        dialog.setMessage("连接成功");
                        dialog.dismiss();
                        dialog = null;
                    }
                    SharePreferenceUtil.put(SharePreferenceUtil.DEVICE_MAC_ADDRESS, gatt.getDevice().getAddress());
                    finish();
                }
            });
        }
    };

    private void scanDevice() {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BluetoothDevice device = result.getDevice();
                Log.d("xuezhiyuan", "找到设备: " + device.getName() + ", " + device.getAddress());
                boolean duplicate = false;
                DeviceBean db;
                if (mDeviceList.size() == 0) {
                    if (TextUtils.isEmpty(device.getName())) {
                        db = new DeviceBean(result.getDevice().getAddress(),result.getDevice().getAddress(),device);
                    } else {
                        db = new DeviceBean(result.getDevice().getName(),result.getDevice().getAddress(),device);
                    }
                    if (db.getDeviceName().contains("CUP")) {
                        mDeviceList.add(db);
                        adapter.setDatas(mDeviceList);
                    }
                }
                for (int i = 0; i < mDeviceList.size(); i++) {
                    if (mDeviceList.get(i).getMacAddress().equals(result.getDevice().getAddress())) {
                        duplicate = true;
                    }
                }
                if (!duplicate) {
                    if (TextUtils.isEmpty(device.getName())) {
                        db = new DeviceBean(result.getDevice().getAddress(),result.getDevice().getAddress(),device);
                    } else {
                        db = new DeviceBean(result.getDevice().getName(),result.getDevice().getAddress(),device);
                    }
                    if (db.getDeviceName().contains("CUP")) {
                        mDeviceList.add(db);
                        adapter.setDatas(mDeviceList);
                    }
                }
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        };
    }
}
