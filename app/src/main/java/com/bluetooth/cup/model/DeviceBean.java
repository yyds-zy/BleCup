package com.bluetooth.cup.model;


import android.bluetooth.BluetoothDevice;

public class DeviceBean {
    private String deviceName;
    private String macAddress;
    private BluetoothDevice device;

    public DeviceBean(String deviceName, String macAddress,BluetoothDevice device) {
        this.deviceName = deviceName;
        this.macAddress = macAddress;
        this.device = device;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public void setDevice(BluetoothDevice device) {
        this.device = device;
    }

    @Override
    public String toString() {
        return "DeviceBean{" +
                "deviceName='" + deviceName + '\'' +
                ", macAddress='" + macAddress + '\'' +
                '}';
    }
}
