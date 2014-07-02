package com.vlctech.metroui;

import com.coship.R;
import com.vlctech.metroui.utils.DensityUtil;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class FocusBorderView extends ImageView {
    private static int X_BORDER_SIZE = 30;
    private static int Y_BORDER_SIZE = 26;
    private static int TRAN_DUR_ANIM = 300;

    private AnimationDrawable mBoxBgAnim;
    private static Animation mBoxAnimNormal;

    public FocusBorderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setBackgroundResource(R.anim.box_normal);
        mBoxAnimNormal = android.view.animation.AnimationUtils.loadAnimation(
                context, R.anim.box_alpha);
        mBoxBgAnim = (AnimationDrawable) this.getBackground();
    }

    public void runTranslateAnimation(View toView) {
        runTranslateAnimation(toView, 1.0F, 1.0F);
    }

//    public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
//        stopBoxAnim();
//        
//        // int [] fromLoc = new int[2];
//        int[] toLoc = new int[2];
//        // this.getLocationOnScreen(fromLoc);
//        toView.getLocationOnScreen(toLoc);
////        int x = toLoc[0] - this.getLeft();
////        int y = toLoc[1] - this.getTop();
//        Log.d("LIF", "toLoc[0] = " + toLoc[0] + ", toLoc[1] = " + toLoc[1]);
//        Log.d("LIF", "this.getLeft() = " + this.getLeft() + ", this.getTop() = " + this.getTop());
//
//        Rect r = new Rect();
//        toView.getGlobalVisibleRect(r);
//        Log.d("LIF", "r.left = " + r.left + ", r.top = " + r.top);
//        
//        toView.getLocationInWindow(toLoc);
//        Log.d("LIF", "toLoc[0] = " + toLoc[0] + ", toLoc[1] = " + toLoc[1]);
//        
//        int x = 0 - this.getLeft();
//        int y = 0 - this.getTop();
//
//        /*
//         * int x = toView.getLeft() - this.getLeft(); int y = toView.getTop() -
//         * this.getTop();
//         */
//        int deltaX = (toView.getWidth() - this.getWidth()) / 2;
//        int deltaY = (toView.getHeight() - this.getHeight()) / 2;
//
//        x = DensityUtil.dip2px(this.getContext(), x + deltaX);
//        y = DensityUtil.dip2px(this.getContext(), y + deltaY);
//
//        float toWidth = toView.getWidth() * scaleX;
//        float toHeight = toView.getHeight() * scaleY;
//        Log.d("LIF",
//                "width = " + toView.getWidth() + ", height = "
//                        + toView.getHeight());
//        float targetScaleX = (float) toWidth
//                / (float) (this.getWidth() - 2 * X_BORDER_SIZE);
//        float targetScaleY = (float) toHeight
//                / (float) (this.getHeight() - 2 * Y_BORDER_SIZE);
//        int width = (int) (toWidth + 2 * X_BORDER_SIZE * targetScaleX);
//        int height = (int) (toHeight + 2 * Y_BORDER_SIZE * targetScaleY);
//
//        flyWhiteBorder(width, height, x, y);
//    }
    
    public void runTranslateAnimation(View toView, float scaleX, float scaleY) {
        Log.d("LIF", "toView.getLeft() = " + toView.getLeft());
        Log.d("LIF", "toView.getTop() = " + toView.getTop());
        View parent = (View) toView.getParent();
        Log.d("LIF", "parent.getLeft() = " + parent.getLeft());
        Log.d("LIF", "parent.getTop() = " + parent.getTop());
        
        Rect rect = new Rect();
        toView.getGlobalVisibleRect(rect);
        Log.d("LIF", "rect.left = " + rect.left);
        Log.d("LIF", "rect.top = " + rect.top);
        
        int x = rect.left - this.getLeft();
        int y = rect.top - this.getTop();
        int deltaX = (toView.getWidth() - this.getWidth()) / 2;
        int deltaY = (toView.getHeight() - this.getHeight()) / 2;
        x = DensityUtil.dip2px(this.getContext(), x + deltaX);
        y = DensityUtil.dip2px(this.getContext(), y + deltaY);

        float toWidth = toView.getWidth() * scaleX;
        float toHeight = toView.getHeight() * scaleY;
        Log.d("LIF",
                "width = " + toView.getWidth() + ", height = "
                        + toView.getHeight());
        float targetScaleX = (float) toWidth
                / (float) (this.getWidth() - 2 * X_BORDER_SIZE);
        float targetScaleY = (float) toHeight
                / (float) (this.getHeight() - 2 * Y_BORDER_SIZE);
        int width = (int) (toWidth + 2 * X_BORDER_SIZE * targetScaleX);
        int height = (int) (toHeight + 2 * Y_BORDER_SIZE * targetScaleY);

        flyWhiteBorder(width, height, x, y);
    }

    /**
     * 停止闪烁动画
     */
    public void stopBoxAnim() {
//        if (mBoxBgAnim.isRunning()) {
//            mBoxBgAnim.stop();
//        }
//        mBoxBgAnim.start();
        mBoxBgAnim.stop();
        this.clearAnimation();
        mBoxAnimNormal.cancel();
    }

    /**
     * 开启闪烁动画
     */
    public void startBoxAnim() {
        startAnimation(mBoxAnimNormal);
        mBoxBgAnim.start();
    }

    /**
     * 白色焦点框飞动、移动、变大
     * 
     * @param width
     *            白色框的宽(非放大后的)
     * @param height
     *            白色框的高(非放大后的)
     * @param paramFloat1
     *            x坐标偏移量，相对于初始的白色框的中心点
     * @param paramFloat2
     *            y坐标偏移量，相对于初始的白色框的中心点
     * */
    @SuppressLint("NewApi")
    private void flyWhiteBorder(int width, int height, float x, float y) {
        this.setVisibility(View.VISIBLE);
        int mWidth = this.getWidth();
        int mHeight = this.getHeight();

        if (mWidth == 0 || mHeight == 0) {
            mWidth = 1;
            mHeight = 1;
        }

        float scaleX = (float) width / (float) mWidth;
        float scaleY = (float) height / (float) mHeight;

        Log.d("LIF", "mWidth = " + mWidth + ", mHeight = " + mHeight);
        Log.d("LIF", "width = " + width + ", height = " + height);
        Log.d("LIF", "x = " + x + ", y = " + y);
        Log.d("LIF", "scaleX = " + scaleX + ", scaleY = " + scaleY);

        animate().translationX(x).translationY(y).setDuration(TRAN_DUR_ANIM)
                .scaleX(scaleX).scaleY(scaleY)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(flyListener).start();
    }

    private Handler mHandler = new Handler();

    Runnable animRunnable = new Runnable() {
        public void run() {
            startBoxAnim();
        }
    };

    @SuppressLint("NewApi")
    private Animator.AnimatorListener flyListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationCancel(Animator arg0) {
        }

        @Override
        public void onAnimationEnd(Animator arg0) {
//             startBoxAnim();
            // stopBoxAnim();
            mHandler.removeCallbacks(animRunnable);
            mHandler.postDelayed(animRunnable, 1000);
        }

        @Override
        public void onAnimationRepeat(Animator arg0) {
        }

        @Override
        public void onAnimationStart(Animator arg0) {
//            stopBoxAnim();
        }

    };
}
