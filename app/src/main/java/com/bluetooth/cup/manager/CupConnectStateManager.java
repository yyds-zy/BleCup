package com.bluetooth.cup.manager;

/**
 * Created by 阿飞の小蝴蝶 on 2023/8/1
 * Describe:
 */
public class CupConnectStateManager {
    private static CupConnectStateManager instance;
    private CupConnectStateManager() {}

    public static CupConnectStateManager getInstance(){
        if (instance == null) {
            synchronized (CupConnectStateManager.class) {
                if (instance == null) {
                    instance = new CupConnectStateManager();
                }
            }
        }
        return instance;
    }

    OnCupConnectStateListener onCupConnectStateListener;

    public void setOnCupConnectStateListener(OnCupConnectStateListener onCupStateListener) {
        this.onCupConnectStateListener = onCupStateListener;
    }

    public void connect() {
        if (onCupConnectStateListener == null) return;
        onCupConnectStateListener.connect();
    }

    public interface OnCupConnectStateListener {
        void connect();
    }
}
