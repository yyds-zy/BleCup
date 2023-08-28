package com.bluetooth.cup.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bluetooth.cup.R;

public class SplashActivity extends AppCompatActivity {

    private boolean isAlphaZero = false;
    private ImageView mImageView;
    private AlphaAnimation alphaAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mImageView = findViewById(R.id.image);
        changeAlpha();
    }

    public void changeAlpha() {
        if (isAlphaZero == true) return;
        //初始化操作，参数传入1和0，即由透明度1变化到透明度为0
        alphaAnimation = new AlphaAnimation(1, 0);
        mImageView.startAnimation(alphaAnimation);//开始动画
        isAlphaZero = true;//标识位
        alphaAnimation.setFillAfter(true);//动画结束后保持状态
        alphaAnimation.setDuration(10);//动画持续时间
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
