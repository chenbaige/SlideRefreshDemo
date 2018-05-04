package com.hbandroid.sliderefreshdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.hbandroid.sliderefreshdemo.adapter.HomeContentAdapter;
import com.hbandroid.sliderefreshdemo.adapter.HomeIndicatorAdapter;
import com.hbandroid.sliderefreshdemo.util.CommonUtil;
import com.hbandroid.sliderefreshdemo.util.GlideImageLoader;
import com.hbandroid.sliderefreshdemo.util.RecyclerViewDivider;
import com.hbandroid.sliderefreshdemo.view.StickyNavLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements StickyNavLayout.OnStickStateChangeListener, OnBannerListener {

    private Banner banner;
    private RecyclerView mIndicatorView;
    private RecyclerView mContentView;

    private SwipeRefreshLayout swipeLayout;
    private StickyNavLayout stickyNavLayout;
    private RelativeLayout snlIindicator;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        banner = findViewById(R.id.banner);
        mIndicatorView = findViewById(R.id.rv_indicator);
        mContentView = findViewById(R.id.rv_content);
        stickyNavLayout = findViewById(R.id.stickyNavLayout);
        swipeLayout = findViewById(R.id.swipeLayout);
        snlIindicator = findViewById(R.id.snlIindicator);
        setBannerData();
        setIndicatorData();
        setContentData();
    }

    /**
     * 设置tab导航栏数据
     */
    private void setIndicatorData() {
        LinearLayoutManager xLinearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mIndicatorView.setLayoutManager(xLinearLayoutManager);
        RecyclerViewDivider divder = new RecyclerViewDivider(false);
        divder.setColor(mContext.getResources().getColor(R.color.trans));
        divder.setDividerHeight(getTabDividerWidth());
        mIndicatorView.addItemDecoration(divder);
        List<String> mUrls = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mUrls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525409191567&di=5dc20cf2404b99e43fa50678d3757776&imgtype=0&src=http%3A%2F%2Fe.hiphotos.baidu.com%2Fexp%2Fw%3D500%2Fsign%3D7645a3a60bd162d985ee621c21dea950%2F8644ebf81a4c510fadb40f786159252dd42aa564.jpg");
        }
        HomeIndicatorAdapter adapter = new HomeIndicatorAdapter(mUrls, mContext);
        mIndicatorView.setAdapter(adapter);
    }

    /**
     * 设置tab导航栏数据
     */
    private void setContentData() {
        LinearLayoutManager xLinearLayoutManager = new LinearLayoutManager(MainActivity.this);
        mContentView.setLayoutManager(xLinearLayoutManager);
        RecyclerViewDivider divder = new RecyclerViewDivider(true);
        divder.setColor(mContext.getResources().getColor(R.color.colorAccent));
        divder.setDividerHeight(1);
        mContentView.addItemDecoration(divder);
        stickyNavLayout.setOnStickStateChangeListener(this);
        List<String> mContents = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mContents.add("浓眉：我只需要上场好好打球 给不给哨是裁判的事");
        }
        HomeContentAdapter adapter = new HomeContentAdapter(mContents, mContext);
        mContentView.setAdapter(adapter);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContentView.getAdapter().notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 获取tab间的间隙
     */
    private int getTabDividerWidth() {
        int totleWidth = getScreenWidth(mContext);
        int tabWidth = dip2px(mContext, 34);
        int leftPadding = dip2px(mContext, 30);
        return (totleWidth - leftPadding * 2 - tabWidth * 5) / 4;
    }

    @Override
    public void isStick(boolean isStick) {

    }

    @Override
    public void scrollPercent(float percent) {
        if (percent == 1) {
            snlIindicator.setBackgroundColor(Color.parseColor("#3F51B5"));
        } else {
            snlIindicator.setBackgroundColor(Color.parseColor((String) CommonUtil.getInstance().evaluate(percent, "#e18e36", "#3F51B5")));
        }
        System.out.println("当前颜色:" + CommonUtil.getInstance().evaluate(percent, "#e18e36", "#3F51B5"));
        if (percent == 0) {
            swipeLayout.setEnabled(true);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mContentView.getAdapter().notifyDataSetChanged();
                    swipeLayout.setRefreshing(false);
                }
            });
        } else {
            swipeLayout.setEnabled(false);
            swipeLayout.setOnRefreshListener(null);
        }
    }

    /**
     * 设置Banner数据
     */
    private void setBannerData() {
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < 6; i++) {
            urls.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1525406238449&di=3847cf21b523ff27772e4d952a3530f5&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F34fae6cd7b899e51fab3e9c048a7d933c8950d21.jpg");
        }
        banner.setImages(urls).setOnBannerListener(this).setImageLoader(new GlideImageLoader()).start();
    }

    @Override
    public void OnBannerClick(int position) {

    }

    /*
   获取屏幕宽度和高度
    */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
