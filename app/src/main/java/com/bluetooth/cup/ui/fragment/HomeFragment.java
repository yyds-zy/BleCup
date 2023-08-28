package com.bluetooth.cup.ui.fragment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluetooth.cup.R;
import com.bluetooth.cup.adapter.RecorderAdapter;
import com.bluetooth.cup.manager.BluetoothGattManager;
import com.bluetooth.cup.manager.CupConnectStateManager;
import com.bluetooth.cup.model.RecordListBean;
import com.bluetooth.cup.ui.BaseFragment;
import com.bluetooth.cup.ui.activity.BleScanActivity;
import com.bluetooth.cup.util.NotifyUtil;
import com.bluetooth.cup.util.SharePreferenceUtil;
import com.bluetooth.cup.view.WaveProgress;

import java.util.List;

public class HomeFragment extends BaseFragment {

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                connectImage.setImageResource(R.mipmap.connect_suc);
                findImage.setImageResource(R.mipmap.search_suc);
                connectTv.setText("已连接");
                long count = RecordListBean.count(RecordListBean.class);
                if (count == 0) {
                    waveProgress.setValue(0);
                } else {
                    RecordListBean recordListBean = RecordListBean.findById(RecordListBean.class, count - 1);
                    waveProgress.setValue(Integer.parseInt(recordListBean.getElectricity()));
                }
            }
        }
    };
    private RecyclerView mRecyclerView;
    private RelativeLayout mAddDeviceLayout,mNotifyLayout,mConnectLayout,mFindCupLayout;
    private ImageView notifyImage,connectImage,findImage;
    private TextView notifyTv,connectTv;
    private WaveProgress waveProgress;
    private RecorderAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_layout_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void init(View view) {

        mNotifyLayout = view.findViewById(R.id.notify_layout);
        mConnectLayout = view.findViewById(R.id.connect_layout);
        mFindCupLayout = view.findViewById(R.id.find_cup_layout);

        notifyImage = view.findViewById(R.id.notify_image);
        notifyTv = view.findViewById(R.id.notify_tv);
        connectImage = view.findViewById(R.id.connect_image);
        connectTv = view.findViewById(R.id.connect_tv);
        findImage = view.findViewById(R.id.find_image);


        boolean notify_state = SharePreferenceUtil.getBoolean("notify_state");
        if (notify_state) {
            notifyImage.setImageResource(R.mipmap.notify_suc);
            notifyTv.setText("关闭通知");
            NotifyUtil.getInstance().setNotifyState(true);
        } else {
            notifyImage.setImageResource(R.mipmap.notify_fail);
            notifyTv.setText("开启通知");
            NotifyUtil.getInstance().setNotifyState(false);
        }

        mNotifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean notify_state = SharePreferenceUtil.getBoolean("notify_state");
                if (notify_state) {
                    SharePreferenceUtil.put("notify_state",false);
                    notifyImage.setImageResource(R.mipmap.notify_fail);
                    notifyTv.setText("开启通知");
                    NotifyUtil.getInstance().setNotifyState(false);
                } else {
                    SharePreferenceUtil.put("notify_state",true);
                    notifyImage.setImageResource(R.mipmap.notify_suc);
                    notifyTv.setText("关闭通知");
                    NotifyUtil.getInstance().setNotifyState(true);
                }
            }
        });

        mConnectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mFindCupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("LED-B");
            }
        });

        mAddDeviceLayout = view.findViewById(R.id.add_device_layout);
        mAddDeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BleScanActivity.class));
            }
        });
        waveProgress = view.findViewById(R.id.wave_progress_bar);
        mRecyclerView =  view.findViewById(R.id.recyclerView);

        List<RecordListBean> dataList = RecordListBean.findWithQuery(RecordListBean.class,"SELECT * FROM RECORD_LIST_BEAN LIMIT ? OFFSET ?;","20","0");


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter = new RecorderAdapter(dataList);
        mRecyclerView.setAdapter(adapter);

        CupConnectStateManager.getInstance().setOnCupConnectStateListener(onCupConnectStateListener);
    }

    CupConnectStateManager.OnCupConnectStateListener onCupConnectStateListener = new CupConnectStateManager.OnCupConnectStateListener() {
        @Override
        public void connect() {
            mHandler.sendEmptyMessage(2);
        }
    };

    private void sendMessage(final String msg) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BluetoothGattService bluetoothGattService = BluetoothGattManager.getInstance().getBluetoothGattService();
                if (bluetoothGattService == null) return;
                BluetoothGattCharacteristic wxGattCharacteristic = BluetoothGattManager.getInstance().getWXGattCharacteristic(bluetoothGattService);
                BluetoothGattManager.getInstance().sendMsg(wxGattCharacteristic,msg.getBytes());
            }
        },50);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<RecordListBean> dataList = RecordListBean.findWithQuery(RecordListBean.class,"SELECT * FROM RECORD_LIST_BEAN LIMIT ? OFFSET ?;","20","0");
        Log.d("xuezhiyuan",dataList.size()+"");
        adapter.notifyDataSetChanged();
    }
}
