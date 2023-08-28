package com.bluetooth.cup.ui.activity;

import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.location.LocationManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bluetooth.cup.R;
import com.bluetooth.cup.listener.BlePermissionListener;
import com.bluetooth.cup.manager.BleManager;
import com.bluetooth.cup.manager.BluetoothGattManager;
import com.bluetooth.cup.ui.BaseActivity;
import com.bluetooth.cup.ui.BaseFragment;
import com.bluetooth.cup.ui.fragment.HomeFragment;
import com.bluetooth.cup.ui.fragment.MeFragment;
import com.bluetooth.cup.ui.fragment.MessageFragment;
import com.bluetooth.cup.util.AnimationUtil;
import com.bluetooth.cup.util.SharePreferenceUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener, BlePermissionListener {

    private ImageView mToolbarIcon;

    ImageView tabHomeImg,tabMsgImg,tabMeImg;
    RelativeLayout tabHomeBg,tabMsgBg,tabMeBg;

    HomeFragment homeFragment;
    MessageFragment messageFragment;
    MeFragment meFragment;

    FrameLayout fragmentLayout;

    BaseFragment mCurFragment;
    RelativeLayout mCurTabIconBg;
    private String mAddress;
    private TextView mToolbarTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        fragmentLayout = findViewById(R.id.fragment_layout);

        tabHomeBg = findViewById(R.id.tab_home_bg);
        tabMsgBg = findViewById(R.id.tab_msg_bg);
        tabMeBg = findViewById(R.id.tab_me_bg);

        tabHomeBg.setOnClickListener(this);
        tabMsgBg.setOnClickListener(this);
        tabMeBg.setOnClickListener(this);

        tabHomeImg = findViewById(R.id.tab_home_img);
        tabMsgImg = findViewById(R.id.tab_msg_img);
        tabMeImg = findViewById(R.id.tab_me_img);

        homeFragment = new HomeFragment();
        mCurTabIconBg = tabHomeBg;
        mCurFragment = homeFragment;
        setTabCheckState(mCurTabIconBg, true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_layout, homeFragment);
        transaction.commit();
        setBlePermissionListener(this);
    }

    private void setTabCheckState(RelativeLayout view, boolean checked) {
        switch (view.getId()) {
            case R.id.tab_home_bg:
                tabHomeImg.setImageResource(checked ? R.mipmap.home_selected_true : R.mipmap.home_selected_false);
                break;
            case R.id.tab_msg_bg:
                tabMsgImg.setImageResource(checked ? R.mipmap.record_selected_true : R.mipmap.record_selected_false);
                break;
            case R.id.tab_me_bg:
                tabMeImg.setImageResource(checked ? R.mipmap.about_selected_true : R.mipmap.about_selected_false);
                break;
        }
    }

    @Override
    protected void initData() {
        BluetoothGattService bluetoothGattService = BluetoothGattManager.getInstance().getBluetoothGattService();
        if (bluetoothGattService == null) {
            AnimationUtil.getInstance().changeAlpha(mToolbarIcon);
        }
        mAddress = SharePreferenceUtil.getString(SharePreferenceUtil.DEVICE_MAC_ADDRESS,"");
        openBle();
        if (!TextUtils.isEmpty(mAddress)) {
            //自动连接 address
            BleManager.getInstance().startScan(scanCallback);
        }
    }

    ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            if (result.getDevice().getAddress().equals(mAddress)) {
                BleManager.getInstance().stopScan(scanCallback);
                BluetoothGattCallback bluetoothGattCallback = BluetoothGattManager.getInstance().getBluetoothGattCallback();
                BluetoothGattManager.getInstance().connectGatt(MainActivity.this,result.getDevice(),bluetoothGattCallback);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && 0 != BluetoothGattManager.getInstance().getDeviceState()) {
            BleManager.getInstance().startScan(scanCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void setToolBar() {
        mToolbarIcon = findViewById(R.id.toolbar_icon);
        mToolbarIcon.setVisibility(View.GONE);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("智能水杯");
    }

    @Override
    public void onClick(View view) {
        if (mCurTabIconBg == view) return;
        setTabCheckState(mCurTabIconBg, false);
        setTabCheckState((RelativeLayout) view, true);
        mCurTabIconBg = (RelativeLayout) view;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(mCurFragment);
        switch (view.getId()) {
            case R.id.tab_home_bg:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fragment_layout, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                mCurFragment = homeFragment;
                mToolbarTitle.setText("智能水杯");
                break;
            case R.id.tab_msg_bg:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.fragment_layout, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                mCurFragment = messageFragment;
                mToolbarTitle.setText("开关记录");
                break;
            case R.id.tab_me_bg:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fragment_layout, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                mCurFragment = meFragment;
                mToolbarTitle.setText("更多产品");
                break;
        }
        transaction.commit();
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
        BleManager.getInstance().stopScan(scanCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
