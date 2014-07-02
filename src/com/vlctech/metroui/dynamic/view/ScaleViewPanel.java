package com.vlctech.metroui.dynamic.view;

import com.coship.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.vlctech.metroui.model.remote.Describe;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

public class ScaleViewPanel extends LinearLayout {
    private static final String TAG = ScaleViewPanel.class.getName();

    private View.OnFocusChangeListener onFocusChangeListener;

    private AsyncImageView image;
    private ImageButton proxy;
    private TextView name;

    private Describe describe;

    private boolean isFocus = false;
    private boolean nameVisiable = false;

    public ScaleViewPanel(Context context) {
        super(context);

        init();
    }

    public ScaleViewPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public AsyncImageView getImage() {
        return image;
    }

    public ImageButton getProxy() {
        return proxy;
    }

    public void setName(String name) {
        this.name.setText(name);
        nameVisiable = TextUtils.isEmpty(name) ? false : true;
    }

    public void setDescribe(Describe describe) {
        this.describe = describe;
        // TODO:
    }

    private void init() {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        image = (AsyncImageView) findViewById(R.id.image);
        proxy = (ImageButton) findViewById(R.id.proxy);
        proxy.setOnFocusChangeListener(proxyFocusListener);
        name = (TextView) findViewById(R.id.name);
    }

    private OnFocusChangeListener proxyFocusListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View view, boolean focus) {
            Log.d("LIF", "in ScaleViewPanel.onFocusChange, v = " + view
                    + ", hasFocus = " + focus);
            if (onFocusChangeListener != null) {
                onFocusChangeListener.onFocusChange(view, focus);
            }
            
            Rect r = new Rect();
            view.getGlobalVisibleRect(r);
            Log.d("LIF", "r.left = " + r.left + ", r.top = " + r.top);

            isFocus = focus;
            showFocusChangeAnimation(view, focus);
            // name.setVisibility(focus ? View.VISIBLE : View.INVISIBLE);
        }

    };

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        onFocusChangeListener = listener;
    }

    /**
     * 获得焦点的item的动画动作
     * 
     * @param paramInt
     *            获得焦点的item
     * */
    private void showFocusChangeAnimation(View v, boolean hasFocus) {
        float scaleFromX = 1.0F;
        float scaleFromY = 1.0F;
        float scaleToX = 1.0F;
        float scaleToY = 1.0F;
        /*
         * float translateFromX = 0F; float translateToX = 0F;
         */
        float translateFromY = 0F;
        float translateToY = 0F;

        if (hasFocus) {
            ScaleViewPanel.this.bringToFront();
            scaleFromX = scaleFromY = 1.0F;
            scaleToX = scaleToY = 1.105F;
            /*
             * translateFromX = -name.getWidth(); translateToX = 0;
             */
            if (nameVisiable) {
                translateFromY = this.getHeight();
                translateToY = 0;

                name.setVisibility(View.VISIBLE);
            }
        } else {
            scaleFromX = scaleFromY = 1.105F;
            scaleToX = scaleToY = 1.0F;
            /*
             * translateFromX = 0; translateToX = name.getWidth();
             */
            if (nameVisiable) {
                translateFromY = 0;
                translateToY = this.getHeight();
            }

            // name.setVisibility(View.INVISIBLE);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(this, "scaleX", scaleFromX,
                scaleToX), ObjectAnimator.ofFloat(this, "scaleY", scaleFromY,
                scaleToY),
        /*
         * ObjectAnimator.ofFloat(name, "translationX", translateFromX,
         * translateToX),
         */
        ObjectAnimator.ofFloat(name, "translationY", translateFromY,
                translateToY));
        set.addListener(new AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                if (isFocus) {
                    ScaleViewPanel.this.bringToFront();
                } else if (nameVisiable) {
                    name.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
            }

            @Override
            public void onAnimationStart(Animator arg0) {
            }

        });
        set.setDuration(250).start();
    }
}
