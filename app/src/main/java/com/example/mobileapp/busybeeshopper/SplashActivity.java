package com.example.mobileapp.busybeeshopper;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {


        /**
         * The thread to process splash screen events
         */
        private Thread mSplashThread;

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Splash screen view
            setContentView(R.layout.splash_activity);

            final SplashActivity sPlashScreen = this;
            final ImageView splashImageView = (ImageView) findViewById(R.id.iv_icons);

            splashImageView.setBackgroundResource(R.drawable.splash_animation);
            final AnimationDrawable frameAnimation = (AnimationDrawable)splashImageView.getBackground();
            splashImageView.post(new Runnable(){
                @Override
                public void run() {
                    frameAnimation.start();
                }
            });

            // The thread to wait for splash screen events
            mSplashThread =  new Thread(){
                @Override
                public void run(){
                    try {
                        synchronized(this){
                            // Wait given period of time or exit on touch
                            wait(1610);
                        }
                    }
                    catch(InterruptedException ex){
                    }

                    finish();

                    // Run next activity
                    Intent intent = new Intent();
                    intent.setClass(sPlashScreen, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            };

            mSplashThread.start();
        }

        /**
         * Processes splash screen touch events
         */
        @Override
        public boolean onTouchEvent(MotionEvent evt)
        {
            if(evt.getAction() == MotionEvent.ACTION_DOWN)
            {
                synchronized(mSplashThread){
                    mSplashThread.notifyAll();
                }
            }
            return true;
        }
    }