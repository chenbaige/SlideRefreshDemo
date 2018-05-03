package com.hbandroid.sliderefreshdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbandroid.twothewin.R;
import com.hbandroid.twothewin.api.Constants;
import com.hbandroid.twothewin.api.common.ApiAsyncTask;
import com.hbandroid.twothewin.api.common.ProjectAPI;
import com.hbandroid.twothewin.api.common.URLManager;
import com.hbandroid.twothewin.ioc.inject.InjectBinder;
import com.hbandroid.twothewin.ioc.inject.InjectInit;
import com.hbandroid.twothewin.ioc.inject.InjectView;
import com.hbandroid.twothewin.ioc.view.listener.OnClick;
import com.hbandroid.twothewin.loader.GlideImageLoader;
import com.hbandroid.twothewin.model.ResponseListEntity;
import com.hbandroid.twothewin.model.discover.Discover_Main_Content_Vo;
import com.hbandroid.twothewin.model.discover.Discover_Main_Tab_Vo;
import com.hbandroid.twothewin.model.match.Match_Main_Banner_Vo;
import com.hbandroid.twothewin.ui.adapter.discover.Discover_Content_Adapter;
import com.hbandroid.twothewin.ui.adapter.discover.Discover_Tab_Adapter;
import com.hbandroid.twothewin.ui.adapter.my.CustomeGridDivider;
import com.hbandroid.twothewin.ui.fragment.LazyFragment;
import com.hbandroid.twothewin.ui.recylerlib.OnItemClickListener;
import com.hbandroid.twothewin.ui.recylerlib.RecyclerHolder;
import com.hbandroid.twothewin.ui.recylerlib.RecyclerViewDivider;
import com.hbandroid.twothewin.ui.view.DiscoverStickyNavLayout;
import com.hbandroid.twothewin.util.CommonUtil;
import com.hbandroid.twothewin.util.Handler_Inject;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:2thewinv3.0
 * <p>
 * Description:立即下单界面
 * <p>
 * <p>
 * Author: baigege(baigegechen@gmail.com)
 * <p>
 * Date：2018-04-11
 */

public class Discover_Main_Fragment extends LazyFragment implements ApiAsyncTask.ApiRequestListener, OnBannerListener, DiscoverStickyNavLayout.OnStickStateChangeListener {

    private Discover_Tab_Adapter mGameImageAdapter = null;
    private Discover_Content_Adapter mContentAdapter = null;

    private int currPage = 1;

    private String mGameId = "";

    private List<Match_Main_Banner_Vo> banners;

    private int totlePage;

    boolean isLoading;

    private Handler handler = new Handler();

    @InjectView
    private com.youth.banner.Banner banner;

    @InjectView
    private RelativeLayout nodata;

    @InjectView
    private TextView tv_match_name;

    @InjectView
    private DiscoverStickyNavLayout stickyNavLayout;

    @InjectView
    private SwipeRefreshLayout swipeLayout;

    @InjectView
    private RecyclerView rv_tab, rv_main_content;

    @InjectView(binders = {@InjectBinder(method = "click", listeners = {OnClick.class})})
    private RelativeLayout rl_order_title;

    @InjectView(binders = {@InjectBinder(method = "click", listeners = {OnClick.class})})
    private ImageView tb_usericon_iv;

    public static Discover_Main_Fragment newInstance() {
        Bundle args = new Bundle();
        Discover_Main_Fragment fragment = new Discover_Main_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectInit
    public void init() {
        onActivityChangeListener.setRightTextState(View.GONE, 0);
        onActivityChangeListener.setRightImageState(View.GONE, 0);
        onActivityChangeListener.setTopTitle(R.string.discover_order_tb_title, "");
        onActivityChangeListener.setTopState(View.GONE);
        onActivityChangeListener.setBottomState(View.VISIBLE);
        setupGameImageAdapter();
        setupContentAdapter();
    }


    public void click(View view) {
        switch (view.getId()) {
            case R.id.tb_usericon_iv:
                onActivityChangeListener.openLeftMenu();
                break;
        }
    }

    protected void request(String gameId) {
        //获取广告数据
        ProjectAPI.commonPostRequest(activity, this, ProjectAPI.ACTION_MATCH_GETTOPAD);
        requestPlayUserList(gameId);

    }

    private void requestPlayUserList() {
        //获取陪玩首页数据接口
        URLManager.get().setUrlparam("page", String.valueOf(currPage));
        ProjectAPI.commonPostRequest(activity, this, ProjectAPI.ACTION_PLAY_PLAYUSERLIST);
    }

    private void requestPlayUserList(String gameId) {
        //获取陪玩首页数据接口
        URLManager.get().setUrlparam("game_id", gameId);
        URLManager.get().setUrlparam("page", String.valueOf(currPage));
        ProjectAPI.commonPostRequest(activity, this, ProjectAPI.ACTION_PLAY_PLAYUSERLIST);
    }

    /**
     * 判断是否刷新列表
     */
    private boolean isRefresh() {
        return currPage == 1;
    }

    @Override
    protected void onFragmentStartLazy() {
        super.onFragmentStartLazy();

        //获取陪玩首页Tab数据接口
        ProjectAPI.commonPostRequest(activity, this, ProjectAPI.ACTION_PLAY_PLAYGAME);

        request("");
    }

    @Override
    protected View getSuccessView() {
        View successView = View.inflate(mContext, R.layout.fragment_discover_main, null);
        Handler_Inject.injectFragment(this, successView);
        return successView;
    }

    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onSuccess(String method, Object obj) {
        object = obj;
        setSuccess();
        ResponseListEntity entity = (ResponseListEntity) obj;
        if (obj != null) {
            switch (method) {
                case ProjectAPI.ACTION_PLAY_PLAYGAME:
                    switch (entity.getStatus()) {
                        case 1:
                            List<Discover_Main_Tab_Vo> vo = getDataList(obj, Discover_Main_Tab_Vo.class);
                            mGameImageAdapter.refreshData(vo);
                            break;
                    }
                    break;
                case ProjectAPI.ACTION_PLAY_PLAYUSERLIST:
                    switch (entity.getStatus()) {
                        case 1:
                            Discover_Main_Content_Vo vo = (Discover_Main_Content_Vo) getData(obj, Discover_Main_Content_Vo.class);
                            totlePage = vo.getPageCount();
                            showContent(true);
                            if (isRefresh()) {
                                //刷新完成
                                swipeLayout.setRefreshing(false);
                                mContentAdapter.refreshData(vo.getGameList());
                            } else {
                                mContentAdapter.addAll(vo.getGameList());
                            }
                            break;
                        case 0:
                            //空数据
                            showContent(false);
                            break;
                    }
                    break;
                case ProjectAPI.ACTION_MATCH_GETTOPAD:
                    switch (entity.getStatus()) {
                        case 0:
                            break;
                        case 1:
                            banners = getDataList(obj, Match_Main_Banner_Vo.class);
                            if (banners != null) {
                                List<String> urls = new ArrayList<String>();
                                for (Match_Main_Banner_Vo banner : banners) {
                                    urls.add(ProjectAPI.getbaseurl() + banner.getImgurl());
                                }
                                banner.setImages(urls).setOnBannerListener(this).setImageLoader(new GlideImageLoader()).start();
                            } else {
                                banner.removeAllViews();
                            }
                            break;
                        case 2:
                            break;
                    }

            }
        }
    }

    private void showContent(boolean b) {
        if (b) {
            rv_main_content.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        } else {
            rv_main_content.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(String method, int statusCode) {

    }

    private void setupGameImageAdapter() {
        LinearLayoutManager xLinearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        rv_tab.setLayoutManager(xLinearLayoutManager);
        RecyclerViewDivider divder = new RecyclerViewDivider(false);
        divder.setColor(mContext.getResources().getColor(R.color.transparent));
        divder.setDividerHeight(getTabDividerWidth());
        rv_tab.addItemDecoration(divder);
        mGameImageAdapter = new Discover_Tab_Adapter(activity);
        mGameImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerHolder holder, int position) {
                mGameId = mGameImageAdapter.getItem(position).getGame_id();
                tv_match_name.setText(mGameImageAdapter.getItem(position).getGameName());
                request(mGameImageAdapter.getItem(position).getGame_id());
            }
        });
        rv_tab.setAdapter(mGameImageAdapter);
    }

    /**
     * 获取tab间的间隙
     */
    private int getTabDividerWidth() {
        int totleWidth = CommonUtil.getScreenWidth(mContext);
        int tabWidth = CommonUtil.dip2px(mContext, 34);
        int leftPadding = CommonUtil.dip2px(mContext, 30);
        return (totleWidth - leftPadding * 2 - tabWidth * 5) / 4;
    }

    private void setupContentAdapter() {
        GridLayoutManager xLinearLayoutManager = new GridLayoutManager(activity, 2);
        rv_main_content.setLayoutManager(xLinearLayoutManager);
        CustomeGridDivider divder = new CustomeGridDivider(mContext, CommonUtil.dip2px(mContext, 10), getMyColor(R.color.page_backcolor));
        rv_main_content.addItemDecoration(divder);
        mContentAdapter = new Discover_Content_Adapter(activity);
        stickyNavLayout.setOnStickStateChangeListener(this);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                request(mGameId);
            }
        });
        mContentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerHolder holder, int position) {
                URLManager.get().setUrlparam("game_id", mContentAdapter.getItem(position).getGame_id());
                URLManager.get().setUrlparam("play_user", mContentAdapter.getItem(position).getUser_id());
                openFragment(new Discover_Card_Fragment());
            }
        });
        rv_main_content.setAdapter(mContentAdapter);
        swipeLayout.setColorSchemeResources(R.color.red, R.color.common_green);
        swipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeLayout.setEnabled(true);
        rv_main_content.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (isVisBottom(recyclerView)) {

                    boolean isRefreshing = swipeLayout.isRefreshing();
                    if (isRefreshing) {
                        mContentAdapter.notifyItemRemoved(mContentAdapter.getItemCount());
                        return;
                    }

                    if (currPage < totlePage) {
                        currPage++;
                        requestPlayUserList();
                    } else {
                        showToast("已经是最后一页了", Constants.TOAST_NORMAL);
                    }
                }
            }
        });
    }

    public static boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数,返回的是不包括回收的子VIew
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition >= totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE &&
                layoutManager.getItemCount() > layoutManager.getChildCount()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public void isStick(boolean isStick) {

    }

    @Override
    public void scrollPercent(float percent) {
        if (percent == 0) {
            swipeLayout.setEnabled(true);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    currPage = 1;
                    request(mGameId);
                }
            });
        } else {
            swipeLayout.setEnabled(false);
            swipeLayout.setOnRefreshListener(null);
        }
    }
}
