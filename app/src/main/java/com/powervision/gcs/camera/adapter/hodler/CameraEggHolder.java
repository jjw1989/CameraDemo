package com.powervision.gcs.camera.adapter.hodler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.powervision.gcs.camera.R;

/**
 * Created by Sundy on 2017/2/16.
 */

public class CameraEggHolder extends BaseViewHolder {
    public TextView tvName;
    public TextView tvValues;
    public ImageView imgArraw;

    public CameraEggHolder(View itemView) {
        super(itemView);
        tvName= (TextView) itemView.findViewById(R.id.tv_item_name);
        tvValues= (TextView) itemView.findViewById(R.id.tv_item_values);
        imgArraw= (ImageView) itemView.findViewById(R.id.setting_type_img);
    }
}
