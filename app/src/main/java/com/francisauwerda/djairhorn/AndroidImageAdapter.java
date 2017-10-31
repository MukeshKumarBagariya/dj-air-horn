package com.francisauwerda.djairhorn;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AndroidImageAdapter extends PagerAdapter {

    Context mContext;

    public AndroidImageAdapter() {}

    public AndroidImageAdapter(Context context) {
        this.mContext = context;
    }

    private int[] sliderImagesId = new int[]{
            R.drawable.thug_life_text, R.drawable.african_kids_dancing_funny_image,
            R.drawable.thug_life_glasses
    };

    public int getImageId(int id) {
        return sliderImagesId[id];
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Object) object);
    }

    @Override
    public int getCount() {
        return sliderImagesId.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView mImageView = new ImageView(mContext);
        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setImageResource(sliderImagesId[position]);
        ((ViewPager) container).addView(mImageView, 0);
        return mImageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}