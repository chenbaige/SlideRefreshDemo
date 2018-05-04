package com.hbandroid.sliderefreshdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbandroid.sliderefreshdemo.R;
import com.hbandroid.sliderefreshdemo.util.GlideImageLoader;

import java.util.List;

/**
 * Title:SlideRefreshDemo
 * <p>
 * Description:
 * <p>
 * <p>
 * Author: baigege(baigegechen@gmail.com)
 * <p>
 * Date：2018-05-04
 */

public class HomeContentAdapter extends RecyclerView.Adapter<HomeContentAdapter.ViewHolder> {

    private List<String> mList;

    private Context mContext;

    private LayoutInflater mInflater;

    public HomeContentAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.activity_content_item, null, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTitleView.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleView = itemView.findViewById(R.id.tv_title);
        }
    }
}
