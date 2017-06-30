package com.powervision.gcs.camera.adapter.hodler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;

/**
 * title com.feishou.fs.adapter
 * description
 * company 北京飞兽科技有限公司
 * version 4.0
 * author Sundy
 * created  2016/3/21 17:07
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder{
    public BaseViewHolder(View itemView) {
        super(itemView);
    }
    @Subscribe(tags = {
            @Tag("test")
    })
    public void test(){

    }
}
