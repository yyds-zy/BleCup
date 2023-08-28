package com.bluetooth.cup.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;


public class AnimationUtil {

    private boolean isAlphaZero = false;
    private AlphaAnimation alphaAnimation_1_0,alphaAnimation_0_1;
    private static AnimationUtil instance;
    private AnimationUtil() {
        alphaAnimation_1_0 = new AlphaAnimation(1, 0);
        alphaAnimation_0_1 = new AlphaAnimation(0, 1);
    }

    public static AnimationUtil getInstance(){
        if (instance == null) {
            synchronized (AnimationUtil.class) {
                if (instance == null) {
                    instance = new AnimationUtil();
                }
            }
        }
        return instance;
    }

    private void alpha_1_0(ImageView imageView) {
        imageView.startAnimation(alphaAnimation_1_0);//开始动画
        alphaAnimation_1_0.setFillAfter(true);//动画结束后保持状态
        alphaAnimation_1_0.setDuration(500);//动画持续时间
    }

    private void alpha_0_1(ImageView imageView) {
        imageView.startAnimation(alphaAnimation_0_1);//开始动画
        alphaAnimation_0_1.setFillAfter(true);//动画结束后保持状态
        alphaAnimation_0_1.setDuration(500);//动画持续时间
    }

    public void changeAlpha(final ImageView imageView) {
        alpha_1_0(imageView);
        alphaAnimation_1_0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               alpha_0_1(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimation_0_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isAlphaZero) return;
                alpha_1_0(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void stopAnimation() {
        isAlphaZero = true;
        if (onDeviceConnectedListener == null) return;
        onDeviceConnectedListener.connected();
    }

    OnDeviceConnectedListener onDeviceConnectedListener;

    public void setOnDeviceConnectedListener(OnDeviceConnectedListener onDeviceConnectedListener) {
        this.onDeviceConnectedListener = onDeviceConnectedListener;
    }

    public interface OnDeviceConnectedListener {
        void connected();
    }
}
