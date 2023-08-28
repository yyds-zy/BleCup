package com.bluetooth.cup.util;


public class CupMessageManager {

    private static CupMessageManager instance;
    private CupMessageManager() {}

    public static CupMessageManager getInstance(){
        if (instance == null) {
            synchronized (CupMessageManager.class) {
                if (instance == null) {
                    instance = new CupMessageManager();
                }
            }
        }
        return instance;
    }

    OnCupStateListener onCupStateListener;

    public void setOnCupStateListener(OnCupStateListener onCupStateListener) {
        this.onCupStateListener = onCupStateListener;
    }

    public void setData(String value) {
        if (onCupStateListener == null) return;
        onCupStateListener.state(value);
    }

    public interface OnCupStateListener {
        void state(String value);
    }
}
