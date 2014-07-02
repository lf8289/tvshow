package com.vlctech.metroui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.coship.R;
import com.vlctech.metroui.dynamic.view.AsyncImageView;
import com.vlctech.metroui.dynamic.view.AutoFillMetroView;
import com.vlctech.metroui.dynamic.view.FocusPagerView;
import com.vlctech.metroui.dynamic.view.MetroViewScroll;
import com.vlctech.metroui.dynamic.view.ScaleViewPanel;
import com.vlctech.metroui.dynamic.view.MetroView.OrientationType;
import com.vlctech.metroui.model.AutoFillMetroItem;
import com.vlctech.metroui.model.local.PageGlobalData;
import com.vlctech.metroui.model.remote.Menu;
import com.vlctech.metroui.model.remote.Sub;

public class HScrollPageCreator {
    private PageGlobalData globalData;

    private HashMap<String, Integer> layoutMap = new HashMap<String, Integer>();

    private LayoutInflater inflate;

    private View.OnClickListener onClickListener;

    public enum BgTypeEnum {
        img("img"), color("color"), action("action"), video("video"), app("app"), adshow(
                "adshow");

        public final String value;

        private BgTypeEnum(String value) {
            this.value = value;
        }

        public static BgTypeEnum fromValue(String value) {
            for (BgTypeEnum style : BgTypeEnum.values()) {
                if (value.equalsIgnoreCase(style.value)) {
                    return style;
                }
            }
            return null;
        }
    }

    public HScrollPageCreator(Context context, PageGlobalData data,
            View.OnClickListener clickListener) {
        globalData = data;
        onClickListener = clickListener;

        layoutMap.put("img",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));

        // TODO: to add these layout
        layoutMap.put("color",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));
        layoutMap.put("video",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));
        layoutMap.put("action",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));
        layoutMap.put("app",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));
        layoutMap.put("adshow",
                Integer.valueOf(R.layout.layout_templet_h_large_rect));

        inflate = LayoutInflater.from(context);
    }
    
    public View getPageView(Context context, List<Sub> listSub) {
        MetroViewScroll pageScroll = new MetroViewScroll(context);
        FocusPagerView container = new FocusPagerView(context);
        AutoFillMetroView page = new AutoFillMetroView(context);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.addView(page, params);

/*        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;*/
        pageScroll.setLayoutParams(params);

        page.setRowHeight(globalData.getCell_height());
        page.setColWidth(globalData.getCell_width());
        page.setOrientation(OrientationType.Horizontal);
        page.setVisibleItems(2, 3);
        page.setPadding(globalData.getCanvas_Left(),
                globalData.getCanvas_Top(), globalData.getCanvas_Left(),
                globalData.getCanvas_Top());

        pageScroll.addView(container);
        
        

        for (Sub sub : listSub) {
            ScaleViewPanel panel = getPanelView(getLayoutFile(sub.getType()));
            setupData(panel, sub);

            page.addAutoFillMetroItem(new AutoFillMetroItem(panel, (int) sub
                    .getRowNum(), (int) sub.getColNum()));
        }

        Log.d("LIF", "------------------width = " + page.getMeasuredHeight()
                + ", height = " + page.getMeasuredWidth());

        return pageScroll;
    }

//    public View getPageView(Context context, List<Sub> listSub) {
//        MetroViewScroll pageScroll = new MetroViewScroll(context);
//        AutoFillMetroView page = new AutoFillMetroView(context);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.CENTER;
//        pageScroll.setLayoutParams(params);
//
//        page.setRowHeight(globalData.getCell_height());
//        page.setColWidth(globalData.getCell_width());
//        page.setOrientation(OrientationType.Horizontal);
//        page.setVisibleItems(2, 3);
//        page.setPadding(globalData.getCanvas_Left(),
//                globalData.getCanvas_Top(), globalData.getCanvas_Left(),
//                globalData.getCanvas_Top());
//
//        pageScroll.addView(page);
//
//        for (Sub sub : listSub) {
//            ScaleViewPanel panel = getPanelView(getLayoutFile(sub.getType()));
//            setupData(panel, sub);
//
//            page.addAutoFillMetroItem(new AutoFillMetroItem(panel, (int) sub
//                    .getRowNum(), (int) sub.getColNum()));
//        }
//
//        Log.d("LIF", "------------------width = " + page.getMeasuredHeight()
//                + ", height = " + page.getMeasuredWidth());
//
//        return pageScroll;
//    }

    public ArrayList<View> getAllPageView(Context context, List<Menu> listMenu) {
        ArrayList<View> views = new ArrayList<View>();

        for (Menu m : listMenu) {
            views.add(getPageView(context, m.getSub()));
        }

        return views;
    }

    private ScaleViewPanel getPanelView(int layout) {
        return (ScaleViewPanel) inflate.inflate(layout, null);
    }

    private int getLayoutFile(String type) {
        int layout = -1;

        if (layoutMap.containsKey(type)) {
            layout = layoutMap.get(type);
        }

        return layout;
    }

    private void setupData(ScaleViewPanel panel, Sub sub) {
        setTagAndClickListener(panel, sub);
        setBackground(panel, sub);
    }

    private void setTagAndClickListener(ScaleViewPanel panel, Sub sub) {
        ImageButton proxy = panel.getProxy();
        if (proxy == null) {
            return;
        }

        proxy.setTag(sub);
        panel.setTag(sub);
        proxy.setOnClickListener(onClickListener);
    }

    private void setBackground(ScaleViewPanel panel, Sub sub) {
        AsyncImageView image = panel.getImage();

        Log.d("LIF", "type = " + BgTypeEnum.fromValue(sub.getType()).ordinal());
        switch (BgTypeEnum.fromValue(sub.getType()).ordinal()) {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            image.loadImage(globalData.getUrlHead() + sub.getBgPic());
            break;
        }

        panel.setName(sub.getName());
        panel.setDescribe(sub.getDescribe());
    }
}
