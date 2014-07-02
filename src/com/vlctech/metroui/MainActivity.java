package com.vlctech.metroui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.coship.R;
import com.vlctech.metroui.JazzyViewPager.TransitionEffect;
import com.vlctech.metroui.model.local.IData;
import com.vlctech.metroui.model.local.ModelImpl;
import com.vlctech.metroui.model.local.PageGlobalData;
import com.vlctech.metroui.model.remote.Menu;
import com.vlctech.metroui.model.remote.Sub;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;

public class MainActivity extends Activity {
    private PageGlobalData globalData;

    private JazzyViewPager viewPager;
    private View root;

    private ArrayList<View> pageViews = new ArrayList<View>();

    private HScrollPageCreator pageCreator;
    private List<Menu> listMenus;

    private ViewTreeObserver mViewTreeObserver;
    private FocusBorderView mBorderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        root = findViewById(R.id.root);
        mViewTreeObserver = root.getViewTreeObserver();
        mViewTreeObserver
                .addOnGlobalFocusChangeListener(globalFocusListener);

        mBorderView = (FocusBorderView) findViewById(R.id.box);
        setupJazziness(TransitionEffect.Accordion);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        TransitionEffect effect = TransitionEffect.Standard;
        switch (keyCode) {
        case KeyEvent.KEYCODE_0:
            effect = TransitionEffect.Standard;
            break;

        case KeyEvent.KEYCODE_1:
            effect = TransitionEffect.Tablet;
            break;

        case KeyEvent.KEYCODE_2:
            effect = TransitionEffect.CubeIn;
            break;

        case KeyEvent.KEYCODE_3:
            effect = TransitionEffect.CubeOut;
            break;

        case KeyEvent.KEYCODE_4:
            effect = TransitionEffect.FlipVertical;
            break;

        case KeyEvent.KEYCODE_5:
            effect = TransitionEffect.FlipHorizontal;
            break;

        case KeyEvent.KEYCODE_6:
            effect = TransitionEffect.ZoomIn;
            break;

        case KeyEvent.KEYCODE_7:
            effect = TransitionEffect.RotateUp;
            break;

        case KeyEvent.KEYCODE_8:
            effect = TransitionEffect.RotateDown;
            break;

        case KeyEvent.KEYCODE_9:
            effect = TransitionEffect.Accordion;
            break;

        default:
            return super.onKeyDown(keyCode, event);
        }

        setupJazziness(effect);
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.d("LIF", "(" + ((Sub) v.getTag()).getName() + ") is clicked");
        }
    };

    private void setupJazziness(TransitionEffect effect) {
        String uiConfig = readUiConfigFile("menuJson.txt");

        globalData = getAllPageData(uiConfig, PageGlobalData.class);

        pageCreator = new HScrollPageCreator(this, globalData,
                itemClickListener);
        // pageViews = creator.getAllPageView(this, globalData.getMenu());

        listMenus = globalData.getMenu();
        pageViews.clear();

        viewPager = (JazzyViewPager) findViewById(R.id.pager);
        viewPager.setTransitionEffect(effect);
        viewPager.setAdapter(new myPagerView());
    }

    private String readUiConfigFile(String name) {
        BufferedInputStream bfs;
        byte[] buffer = new byte[1024];
        String menu = "";
        try {
            bfs = new BufferedInputStream(getAssets().open(name));
            int bytes = 0;
            while ((bytes = bfs.read(buffer)) != -1) {
                menu += new String(buffer, 0, bytes);
            }
            bfs.read(buffer);
            bfs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return menu;
    }

    public PageGlobalData getAllPageData(String content, Class<?> cls) {
        IData data = new ModelImpl(cls).doParser(content);
        boolean success = false;
        PageGlobalData pgd = null;

        if (data != null) {
            success = data instanceof PageGlobalData;
            if (success) {
                pgd = (PageGlobalData) data;
            }
        }

        return pgd;
    }

    class myPagerView extends PagerAdapter {
        @Override
        public int getCount() {
            return listMenus.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            if (view instanceof OutlineContainer) {
                return ((OutlineContainer) view).getChildAt(0) == obj;
            } else {
                return view == obj;
            }
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            if (arg1 < pageViews.size()) {
                ((ViewGroup) arg0).removeView(pageViews.get(arg1));
            }
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            View page = null;
            if (pageViews.size() > arg1) {
                page = pageViews.get(arg1);
            } else {
                page = pageCreator.getPageView(MainActivity.this, listMenus
                        .get(arg1).getSub());
                pageViews.add(page);
            }
            ((ViewGroup) arg0).addView(page, 0);
            viewPager.setObjectForPosition(page, arg1);

            /*mViewTreeObserver = page.getViewTreeObserver();
            mViewTreeObserver
                    .addOnGlobalFocusChangeListener(globalFocusListener);*/

            return page;
        }

    }

    private Handler mHandler = new Handler();
    private View curFocusView = null;
    
    private Runnable focusMoveRunnable = new Runnable() {

        @Override
        public void run() {
            mBorderView.runTranslateAnimation(curFocusView, 1.105F, 1.105F);
        }
        
    };
    
    private OnGlobalFocusChangeListener globalFocusListener = new OnGlobalFocusChangeListener() {

        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//            Log.d("LIF", "oldFocus = " + oldFocus);
            Log.d("LIF", "newFocus = " + newFocus);
            
/*            if (curFocusView == newFocus) {
                return ;
            }*/
            
            curFocusView = newFocus;
            
//            Log.d("LIF", "viewPager.getWidth() = " + viewPager.getWidth());
//            Log.d("LIF", "viewPager.getLeft() = " + viewPager.getLeft() + ", viewPager.getTop() = " + viewPager.getTop());
            
/*            mHandler.removeCallbacks(focusMoveRunnable);
            mHandler.postDelayed(focusMoveRunnable, 1000);*/
            mBorderView.runTranslateAnimation(curFocusView, 1.105F, 1.105F);
        }

    };
}
