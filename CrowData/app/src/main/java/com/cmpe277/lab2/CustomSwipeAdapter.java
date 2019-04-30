package com.cmpe277.lab2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomSwipeAdapter extends PagerAdapter {

    private  Context mContext;
    private  ArrayList<Uri> mImageList;
    private  LayoutInflater mLayoutInflater;
    private  final String TAG = "ImageUplaodActivity";

    CustomSwipeAdapter(Context context, ArrayList<Uri> ImageList, Button uploadButton)
    {
        mContext = context;
        mImageList = ImageList;
    }

    @Override
    public int getCount() {
        Log.d(TAG,"Returning "+mImageList.size()+" elements");
        return mImageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return (view == (LinearLayout)o);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Log.d(TAG,"Instantiating position: "+position);
        mLayoutInflater = (LayoutInflater)mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = mLayoutInflater.inflate(R.layout.swipe_layout,
                collection, false);
        ImageView imageView = item_view.findViewById(R.id.upload_image);
        Glide.with(mContext).load(mImageList.get(position)).into(imageView);
        collection.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


}
