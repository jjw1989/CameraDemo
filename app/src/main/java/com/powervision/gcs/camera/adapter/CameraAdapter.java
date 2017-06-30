package com.powervision.gcs.camera.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.powervision.gcs.camera.R;
import com.powervision.gcs.camera.adapter.hodler.BaseViewHolder;
import com.powervision.gcs.camera.adapter.hodler.CameraEggHolder;
import com.powervision.gcs.camera.model.CameraModel;

import java.util.List;

/**
 * 相机参数适配
 * Created by Sundy on 2017/6/15.
 */

public class CameraAdapter extends CommonAdapter<CameraModel>{
    public CameraAdapter(List<CameraModel> mDatas) {
        super(mDatas);
    }

    @Override
    protected void bindViewAndDatas(BaseViewHolder holder, CameraModel cameraModel) {
        CameraEggHolder cameraEggHolder= (CameraEggHolder) holder;
        cameraEggHolder.tvName.setText(cameraModel.getName());
        cameraEggHolder.tvValues.setText(cameraModel.getValue());

    }

    @Override
    public View createView(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gcs_camera_item_layout,viewGroup,false);
        return view;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
         return new CameraEggHolder(view);
    }
}
