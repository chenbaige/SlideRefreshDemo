package com.hbandroid.sliderefreshdemo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hbandroid.sliderefreshdemo.R;
import com.joooonho.SelectableRoundedImageView;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .crossFade()
                .into(imageView);
//        ImageUtils.downloadHomeTopDrawable(context.getApplicationContext(),(String)path,imageView);
    }

    /**
     * 加载网络图片
     */
    public static void LoadUrlImage(Context context,String url,ImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }

    /**
     * 加载网络图片
     */
    public static void LoadUrlImage(Context context,String url,SelectableRoundedImageView imageView){
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
    }
}
