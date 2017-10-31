package com.francisauwerda.djairhorn;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnTouchListener {
    private SoundPool soundPool;
    private int soundID;
    boolean loaded = false;
    private Button mButtonMaxVolume;
    private ImageView mIvBackground;
    private ImageView mIvTrump;
    private RelativeLayout mRlTop;
    private RelativeLayout mRlMiddle;
    private RelativeLayout mRlBottom;
    private RelativeLayout mRlTrump;
    private ViewPager mViewPager;
    private AndroidImageAdapter mAndroidImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIvBackground = (ImageView) findViewById(R.id.iv_background);
        mIvTrump = (ImageView) findViewById(R.id.iv_trump);
        mButtonMaxVolume = (Button) findViewById(R.id.bt_max_volume);
        mRlTop = (RelativeLayout) findViewById(R.id.rl_top);
        mRlMiddle = (RelativeLayout) findViewById(R.id.rl_middle);
        mRlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        mRlTrump = (RelativeLayout) findViewById(R.id.rl_trump);

        setUpMaxVolumeButton();

        View view = findViewById(R.id.siren_button);
        view.setOnTouchListener(this);
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        buildSoundPool();

        mViewPager = (ViewPager) findViewById(R.id.vp_viewPager);
        mAndroidImageAdapter = new AndroidImageAdapter(this);
        mViewPager.setAdapter(mAndroidImageAdapter);
    }

    /**
     * Creates a SoundPool object with the dj air horn sound file.
     */
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

                int currentImageId = mViewPager.getCurrentItem();
                mIvTrump.setImageResource(mAndroidImageAdapter.getImageId(currentImageId));

                hideEverything();
                flickTheLights();
            }

        }
        return false;
    }

    private void flickTheLights() {
        // Create the animation that flickers.
        Animation anim1 = new AlphaAnimation(0.0f, 1.0f);
        anim1.setDuration(50); //You can manage the time of the blink with this parameter
        anim1.setStartOffset(20);
        anim1.setRepeatMode(Animation.REVERSE);
        anim1.setRepeatCount(30);

        Animation anim2 = new AlphaAnimation(0.0f, 1.0f);
        anim2.setDuration(50); //You can manage the time of the blink with this parameter
        anim2.setRepeatMode(Animation.REVERSE);
        anim2.setRepeatCount(30);

        mIvBackground.startAnimation(anim1);
        mIvTrump.startAnimation(anim2);

        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showEverything();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * When the trump button is clicked this function will hide everything on the screen and
     * then show the flashing colours and images.
     */
    private void hideEverything() {
        mRlTop.setVisibility(View.GONE);
        mRlMiddle.setVisibility(View.GONE);
        mRlBottom.setVisibility(View.GONE);
        mRlTrump.setVisibility(View.VISIBLE);
    }

    /**
     * After the flashing colours and images animation has ended then we want to hide their objects
     * and show our original buttons.
     */
    private void showEverything() {
        mRlTop.setVisibility(View.VISIBLE);
        mRlMiddle.setVisibility(View.VISIBLE);
        mRlBottom.setVisibility(View.VISIBLE);
        mRlTrump.setVisibility(View.GONE);
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

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.release();
        soundPool = null;
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

}