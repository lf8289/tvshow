package com.vlctech.metroui.dynamic.view;

import com.vlctech.metroui.FocusBorderView;

import android.content.Context;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import android.widget.FrameLayout;

public class FocusPagerView extends FrameLayout {

//    FocusBorderView mBorderView;

    private ViewTreeObserver mViewTreeObserver;

    private View curFocusView = null;

    public FocusPagerView(Context context) {
        super(context);
        init(context);
    }

    public FocusPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FocusPagerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setClipChildren(false);
        setClipToPadding(false);

/*        mBorderView = new FocusBorderView(context, null);
        LayoutParams layoutparams = new LayoutParams(220, 220);
        layoutparams.leftMargin = 0;
        layoutparams.topMargin = 0;
        addView(mBorderView, layoutparams);*/

        mViewTreeObserver = getViewTreeObserver();
        mViewTreeObserver.addOnGlobalFocusChangeListener(globalFocusListener);
    }

    private OnGlobalFocusChangeListener globalFocusListener = new OnGlobalFocusChangeListener() {

        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            // Log.d("LIF", "oldFocus = " + oldFocus);
            Log.d("LIF", "newFocus = " + newFocus);

            /*
             * if (curFocusView == newFocus) { return ; }
             */

            curFocusView = newFocus;

            /*
             * mHandler.removeCallbacks(focusMoveRunnable);
             * mHandler.postDelayed(focusMoveRunnable, 1000);
             */
/*            mBorderView.bringToFront();
            mBorderView.runTranslateAnimation(curFocusView, 1.105F, 1.105F);*/
        }

    };

}
