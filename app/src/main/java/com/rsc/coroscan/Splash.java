package com.rsc.coroscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.rsc.coroscan.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {
    ActivitySplashBinding bdx;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ctx = this;
        bdx = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        //start animation

        //display more
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //new animation
                bdx.lottie.cancelAnimation();
                bdx.lottie.setAnimation(R.raw.stay_safe);
                bdx.lottie.playAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //animation two
                        bdx.lottie.cancelAnimation();
                        bdx.lottie.setAnimation(R.raw.covid_3m);
                        bdx.lottie.playAnimation();
                        //change text
                        bdx.welcomeTxt.setText("Keep Social Distance");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(Splash.this, MainActivity.class));
                                finish();
                            }
                        }, 4000);
                    }
                }, 4000);
            }
        }, 4000);
    }
}
