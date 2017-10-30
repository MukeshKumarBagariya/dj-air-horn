package com.francisauwerda.djairhorn;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnTouchListener {
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    private Button mButtonMaxVolume;
    private ImageView mIv1;
    private Button mSirenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv1 = (ImageView) findViewById(R.id.iv_1);
        mSirenButton = (Button) findViewById(R.id.siren_button);
        mButtonMaxVolume = (Button) findViewById(R.id.bt_max_volume);
        setUpMaxVolumeButton();

        View view = findViewById(R.id.siren_button);
        view.setOnTouchListener(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        buildSoundPool();
    }

    private void buildSoundPool() {
        if (soundPool == null) {
            soundPool = new SoundPool.Builder().build();
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    loaded = true;
                }
            });
            soundID = soundPool.load(this, R.raw.dj_air_horn_multiple, 1);
        }
    }

    private void destroySoundPool() {
        soundPool.release();
        soundPool = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

            float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;

            if (loaded) {
                soundPool.play(soundID, volume, volume, 1, 0, 1f);
                Log.v("Sound", "Sound played");
                flickTheLights();
            }

        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroySoundPool();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildSoundPool();
    }

    @Override
    protected void onStart() {
        super.onStart();
        buildSoundPool();
    }

    private void flickTheLights() {
        mSirenButton.setVisibility(View.GONE);
        mButtonMaxVolume.setVisibility(View.GONE);
        mIv1.setVisibility(View.VISIBLE);

        // Create the animation that flickers.
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(50); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(30);

        mIv1.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSirenButton.setVisibility(View.VISIBLE);
                mButtonMaxVolume.setVisibility(View.VISIBLE);
                mIv1.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Sets the devices volume to MAX.
     */
    public void setMaxVolume() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        Log.v("Volume", "Volume has been set to MAX");
    }

    /**
     * Sets up the max volume button.
     */
    private void setUpMaxVolumeButton() {
        mButtonMaxVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaxVolume();
            }
        });
    }


}