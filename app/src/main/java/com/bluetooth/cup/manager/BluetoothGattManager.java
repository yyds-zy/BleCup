package com.bluetooth.cup.manager;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.bluetooth.cup.constant.Constant;
import com.bluetooth.cup.util.CupMessageManager;
import com.bluetooth.cup.util.MiscUtil;
import com.bluetooth.cup.util.SharePreferenceUtil;

import java.util.List;
import java.util.UUID;

/**
 * BluetoothGattManager管理器
 */
public class BluetoothGattManager {

    private static BluetoothGattManager instance;
    private BluetoothGatt mGatt;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattManager() {}

    public static BluetoothGattManager getInstance(){
        if (instance == null) {
            synchronized (BluetoothGattManager.class) {
                if (instance == null) {
                    instance = new BluetoothGattManager();
                }
            }
        }
        return instance;
    }

    public void connectGatt(Context context, BluetoothDevice device, BluetoothGattCallback bluetoothGattCallback) {
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mGatt = device.connectGatt(context,false,bluetoothGattCallback,BluetoothDevice.TRANSPORT_LE);
            } else {
                mGatt = device.connectGatt(context,false,bluetoothGattCallback);
            }
        }
    }

    /**
     * 设备状态
     * @return
     */
    public int getDeviceState() {
        if (mGatt == null) return -1;
        boolean connect = mGatt.connect();
        if (connect) {
            return 0;
        } else {
            return -1;
        }
    }

    public void setBluetoothGattService(BluetoothGattService bluetoothGattService) {
        mBluetoothGattService = bluetoothGattService;
    }

    /**
     * 获取GattService
     * @return
     */
    public BluetoothGattService getBluetoothGattService() {
        return mBluetoothGattService;
    }

    /**
     * 获取对应Gatt服务的写入数据特征
     * @param service
     * @return
     */
    public BluetoothGattCharacteristic getWXGattCharacteristic(BluetoothGattService service) {
        String write = SharePreferenceUtil.getString(SharePreferenceUtil.SERVICE_UUID_WRITE, "");
        if (TextUtils.isEmpty(write)) return null;
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString(write));
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        return characteristic;
    }

    /**
     * 获取对应Gatt服务的读取数据特征
     * @param
     * @return
     */
    public BluetoothGattCharacteristic getRXGattCharacteristic() {
        String notify = SharePreferenceUtil.getString(SharePreferenceUtil.SERVICE_UUID_NOTIFY, "");
        if (TextUtils.isEmpty(notify)) return null;
        BluetoothGattCharacteristic characteristic = mBluetoothGattService.getCharacteristic(UUID.fromString(notify));
        mGatt.setCharacteristicNotification(characteristic, true);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(Constant.default_uuid));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mGatt.writeDescriptor(descriptor);
        Log.d("xuezhiyuan","getRXGattCharacteristic");
        return characteristic;
    }

    /**
     * 发送数据
     * @param writeCharacteristic
     * @param value
     */
    public void sendMsg(BluetoothGattCharacteristic writeCharacteristic,byte[] value) {
        writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        writeCharacteristic.setValue(value);
        mGatt.writeCharacteristic(writeCharacteristic);
    }

    public BluetoothGattCallback getBluetoothGattCallback() {
        return mBluetoothGattCallback;
    }

    BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == 0 && newState == 2) {
                //连接成功
                gatt.requestMtu(160);
            }
        }

        @Override
        public void onMtuChanged(final BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("xuezhiyuan","连接成功" + mtu);
                gatt.discoverServices();
                SharePreferenceUtil.put(SharePreferenceUtil.DEVICE_CONNECT_STATE,true);
                CupConnectStateManager.getInstance().connect();
                mGattCallBack.onMtuChanged(gatt);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            List<BluetoothGattService> services = gatt.getServices();
            for (int i = 0; i < services.size(); i++) {
                UUID uuid = services.get(i).getUuid();
                if (uuid.toString().contains(Constant.service_uuid_prefix)) {
                    Log.d("xuezhiyuan", " service "+uuid.toString());
                    SharePreferenceUtil.put(SharePreferenceUtil.SERVICE_UUID,uuid.toString());
                    BluetoothGattService service = gatt.getService(uuid);
                    BluetoothGattManager.getInstance().setBluetoothGattService(service);
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        if (characteristic.getUuid().toString().contains(Constant.notify_uuid_prefix)) {
                            SharePreferenceUtil.put(SharePreferenceUtil.SERVICE_UUID_NOTIFY,characteristic.getUuid().toString());
                            Log.d("xuezhiyuan"," notify characteristic " + characteristic.getUuid().toString());
                        } else if (characteristic.getUuid().toString().contains(Constant.write_uuid_prefix)) {
                            SharePreferenceUtil.put(SharePreferenceUtil.SERVICE_UUID_WRITE,characteristic.getUuid().toString());
                            Log.d("xuezhiyuan"," write characteristic " + characteristic.getUuid().toString());
                        }
                    }
                }
            }
            BluetoothGattManager.getInstance().getRXGattCharacteristic();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();
            if (value.length == 20) {
                Log.d("xuezhiyuan", "接收数据: " + MiscUtil.byte2String(value));
                CupMessageManager.getInstance().setData(MiscUtil.byte2String(value));
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            byte[] value = characteristic.getValue();
            Log.d("xuezhiyuan"," onCharacteristicRead " + new String(value));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d("xuezhiyuan","发送数据成功");
        }


        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            byte[] value = descriptor.getValue();
            Log.d("xuezhiyuan", " onDescriptorRead " + new String(value));
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e("xuezhiyuan", "开启监听成功");
        }
    };

    public void setGattCallback(GattCallBack gattCallback) {
        mGattCallBack = gattCallback;
    }

    GattCallBack mGattCallBack;

    public interface GattCallBack {
        void onMtuChanged(BluetoothGatt gatt);
    }
}
