package com.powervision.gcs.camera.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.powervision.gcs.camera.R;
import com.powervision.gcs.camera.ui.fgt.media.MediaImgListFragment;
import com.powervision.gcs.camera.ui.fgt.media.MediaVideoListFragment;


/**
 * 媒体库视频适配器
 * Created by sundy on 2016/6/24.
 */
public class MediaAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private static final String[] TITLES = new String[] {
            "图片", "视频"
    };

    public MediaAdapter(FragmentManager fm) {
        super(fm);
    }

    public MediaAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position) {
            case 0:// 导入图片列表
                fragment = new MediaImgListFragment();
                break;
            case 1:// 导入视频列表
                fragment = new MediaVideoListFragment();
                break;
        }

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return new String[]{mContext.getString(R.string.media_imageview_txt), mContext.getString(R.string.media_video_txt)}[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
