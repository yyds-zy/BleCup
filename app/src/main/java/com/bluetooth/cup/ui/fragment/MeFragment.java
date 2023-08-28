package com.bluetooth.cup.ui.fragment;

import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.bluetooth.cup.R;
import com.bluetooth.cup.manager.BluetoothGattManager;
import com.bluetooth.cup.ui.BaseFragment;
import com.bluetooth.cup.util.TimeUtils;
import android.os.Handler;


public class MeFragment extends BaseFragment implements View.OnClickListener {

    Button closeAppBtn;
    private Handler mHandler = new Handler();
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_layout_me;
    }

    @Override
    protected void init(View view) {
        closeAppBtn = view.findViewById(R.id.close_app);
        closeAppBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_app:
                showCloseAppDialog();
                break;
        }
    }

    private void syncClock() {
        String clock = TimeUtils.getCurrentTime();
        Log.d("xuezhiyuan"," clock " + clock);
        BluetoothGattService bluetoothGattService = BluetoothGattManager.getInstance().getBluetoothGattService();
        if (bluetoothGattService == null) return;
        BluetoothGattCharacteristic wxGattCharacteristic = BluetoothGattManager.getInstance().getWXGattCharacteristic(bluetoothGattService);
        BluetoothGattManager.getInstance().sendMsg(wxGattCharacteristic,clock.getBytes());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        },3000);
    }

    private void showCloseAppDialog() {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
        normalDialog.setCancelable(false);
        normalDialog.setMessage("请问是否要同步水杯时间，并退出应用？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        syncClock();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        normalDialog.show();
    }
}
