package com.vlctech.metroui.dynamic.view;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.vlctech.metroui.utils.ImageLoaderHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class AsyncImageView extends ImageView {

    private ImageLoaderHelper loadHelper;

    public AsyncImageView(Context context) {
        super(context);
        init();
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        loadHelper = ImageLoaderHelper.getInstance(getContext());
    }

    public void loadImage(String url) {
        loadHelper.displayImage(url, this, listener);
    }

    private ImageLoaderHelper.ImageLoaderHelperListener listener = new ImageLoaderHelper.ImageLoaderHelperListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view,
                FailReason failReason) {
        }

        @Override
        public void onLoadingComplete(String imageUri, View view,
                Bitmap loadedImage) {
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
        }
    };
}
