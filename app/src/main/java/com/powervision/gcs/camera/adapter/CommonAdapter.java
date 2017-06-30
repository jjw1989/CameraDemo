package com.powervision.gcs.camera.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.powervision.gcs.camera.adapter.hodler.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * title RecyclerView adapter
 * description
 * company 北京飞兽科技有限公司
 * version 4.0
 * author Sundy
 * created  2016/3/21 17:01
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> mDatas;
    private int position;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;
    public Context mContent;
    public int selectIndex=-1;

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        if (mDatas != null)
            this.mDatas = mDatas;
        else
            this.mDatas = new ArrayList<>();
    }

    /**
     * 得到当前的位置
     *
     * @return
     */
    public int getPosition() {
        return position;
    }
    public CommonAdapter( List<T> mDatas){
        setDatas(mDatas);
    }

    public CommonAdapter(Context context, List<T> mDatas) {
        this.mContent=context;
        setDatas(mDatas);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mContent==null){
            mContent = parent.getContext();
        }
        View view = createView(parent, viewType);
        BaseViewHolder hodler = createViewHolder(view);
        return hodler;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder hodler, final int position) {
        this.position = position;
        bindViewAndDatas(hodler, mDatas.get(position));
        hodler.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener!=null) {
                    onRecyclerItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    /**
     * 将数据绑定在视图上
     *
     * @param holder
     * @param t
     */
    protected abstract void bindViewAndDatas(BaseViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 加载item的view,直接返回加载的view即可
     *
     * @param viewGroup 如果需要Context,可以viewGroup.getContext()获取
     * @param i
     * @return item 的 view
     */
    public abstract View createView(ViewGroup viewGroup, int i);

    /**
     * 加载一个ViewHolder,为RecyclerViewHolderBase子类,直接返回子类的对象即可
     *
     * @param view item 的view
     * @return RecyclerViewHolderBase 基类ViewHolder
     */
    public abstract BaseViewHolder createViewHolder(View view);

    /**
     * 添加到第一个位置
     *
     * @param model
     */
    public void addFirstItem(T model) {
        this.addItem(0, model);
    }

    /**
     * 动态增加一条指定位置数据
     *
     * @param position
     * @param model
     */
    public void addItem(int position, T model) {
        checkListNull();
        this.mDatas.add(position, model);
        this.notifyItemInserted(position);
    }

    /**
     * 添加到最后一个位置
     * @param model
     */
    public void addLastItem(T model) {
        checkListNull();
        this.addItem(this.mDatas.size(), model);
    }
//    public void setDatas(List<T> datas) {
//        if(datas != null) {
//            this.mDatas = datas;
//        } else {
//            this.mDatas.clear();
//        }
//
//        this.notifyDataSetChanged();
//    }
    /**
     * 添加新数据
     *
     * @param datas
     */

    public void addNewDatas(List<T> datas) {
        if (datas==null){
            return;
        }
        checkListNull();
        this.mDatas.addAll(0, datas);
        this.notifyItemRangeInserted(0, datas.size());
    }
    public void changeDate(List<T> datas){
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
        Log.i("qwert", "changeDate: .........");
    }
    public void refreshWaypoint(List<T> mDatas){

    }
    /**
     * 添加更多数据
     * @param datas
     */
    public void addMoreDatas(List<T> datas) {
        if (datas != null) {
            this.mDatas.addAll(this.mDatas.size(), datas);
            this.notifyItemRangeInserted(this.mDatas.size(), datas.size());
        }

    }

    /**
     * 新model替换旧model
     * @param oldModel
     * @param newModel
     */
    public void setItem(T oldModel, T newModel) {
        this.setItem(this.mDatas.indexOf(oldModel), newModel);
    }

    /**
     * 指定位置添加model
     * @param location
     * @param newModel
     */
    public void setItem(int location, T newModel) {
        this.mDatas.set(location, newModel);
        this.notifyItemChanged(location);
    }
    /**
     * 删除制定位置数据
     *
     * @param position
     */
    public void removeItem(int position) {
        if (mDatas.isEmpty()) {
            return;
        }
        this.mDatas.remove(position);
        this.notifyItemRemoved(position);
    }

    /**
     * 删除航点
     * @param position
     */
    public void deleteWaypoint(int position){
        if(position==selectIndex){
            selectIndex=-1;
        }
        this.mDatas.remove(position);
        notifyDataSetChanged();
    }
    /**
     * 移除数据中指定的model
     * @param model
     */
    public void removeItem(T model) {
        this.removeItem(this.mDatas.indexOf(model));
    }

    /**
     * 指定开始位置和结束位置移除数据
     * @param fromPosition
     * @param toPosition
     */
    public void moveItem(int fromPosition, int toPosition) {
        this.mDatas.add(toPosition, this.mDatas.remove(fromPosition));
        this.notifyItemMoved(fromPosition, toPosition);
    }
    /**
     * 清空所有数据
     */
    public void clear() {
        if (mDatas == null && mDatas.isEmpty()) {
            return;
        }
        this.mDatas.clear();
        this.notifyDataSetChanged();
    }

    public void checkListNull() {
        if (mDatas == null) {
            mDatas = new ArrayList<T>();
        }
    }
}
