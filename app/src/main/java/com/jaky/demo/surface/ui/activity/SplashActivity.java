package com.jaky.demo.surface.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jaky.demo.surface.R;
import com.jaky.demo.surface.data.binding.ActivitySplashModel;
import com.jaky.demo.surface.databinding.ActivitySplashBinding;
import com.onyx.android.sdk.data.util.ActivityUtil;

/**
 * Created by Jack on 2017/12/31.
 */

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding bindingView;
    private ActivitySplashModel splashModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        splashModel = new ActivitySplashModel(SplashActivity.this);
        bindingView.setSplashModel(splashModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playAnimate();
    }

    private void playAnimate() {
        bindingView.splashImage.setImageResource(splashModel.getBackgroundImageResID());
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ActivityUtil.startActivitySafely(SplashActivity.this, new Intent(SplashActivity.this, ReaderActivity.class));
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        bindingView.splashImage.startAnimation(animation);

    }

}
