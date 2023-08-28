package com.bluetooth.cup.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluetooth.cup.util.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class CommonListAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private static final String TAG = CommonListAdapter.class.getSimpleName();
    protected Context mContext;
    protected List<T> mDatas;
    protected int mLayoutId;
    protected LayoutInflater mInflater;
    protected ViewGroup mRv;
    private OnItemClickListener mOnItemClickListener;
    private OnItemFoucusChangeListener mOnItemFocusChangeListener;

    public CommonListAdapter(Context context, List<T> datas, int layoutId) {
        mContext = Objects.requireNonNull(context,"context dispatch into CommonListAdapter can't be null");
        mInflater = LayoutInflater.from(context);
        if (layoutId == 0)
            Log.e(TAG,"please input valid layoutId parameter");
        mLayoutId = layoutId;
        mDatas = datas;
    }

    public CommonListAdapter setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public OnItemClickListener getmOnItemClickListener() {
        return mOnItemClickListener;
    }

    @NonNull
    @Override
    public CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mRv == null) {
            mRv = parent;
        }
        CommonViewHolder holder = CommonViewHolder.get(mContext, parent, mLayoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
        setListener(position, holder);
        if (mDatas != null) {
            convert(holder, mDatas.get(position));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            if (mDatas != null) {
                Bundle bundle = (Bundle) payloads.get(0);

                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        if (!TextUtils.isEmpty(key)) {
                            diffConvert(holder, mDatas.get(position), key);
                        }
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public abstract void diffConvert(CommonViewHolder holder, T t, String whereDirty);

    protected void setListener(final int position, final CommonViewHolder viewHolder) {
        if (!isEnabled(getItemViewType(position))) return;
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mRv, v, mDatas.get(position), position);
                }
            }
        });


        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    return mOnItemClickListener.onItemLongClick(mRv, v, mDatas.get(position), position);
                }
                return false;
            }
        });

        viewHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.d(TAG,"onFocusChange :"+ hasFocus);
               // Preconditions.checkNotNull(mOnItemFocusChangeListener,"ItemFoucusListener can not be null");
                if(mOnItemFocusChangeListener != null)
                mOnItemFocusChangeListener.onFocusChange(view, hasFocus,viewHolder);
            }
        });
    }

    public abstract void convert(CommonViewHolder holder, T t);

    protected boolean isEnabled(int itemViewType) {
        return true;
    }



    public void setOnItemFocusChangeListener(@NonNull OnItemFoucusChangeListener foucusChangeListener){
        this.mOnItemFocusChangeListener = foucusChangeListener;
    }
    public int getPosition(RecyclerView.ViewHolder viewHolder) {
        Preconditions.checkNotNullValue(viewHolder,
                "viewHolder is null when get position of viewHolder");
        return viewHolder.getAdapterPosition();
    }

    /**
     * 删除单个数据
     *
     * @param i
     */
    public void remove(int i) {
        if (mDatas != null && mDatas.size() > i && i > -1) {
            mDatas.remove(i);
            notifyDataSetChanged();
        }
    }

    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 初始化并刷新数据
     *
     * @param list
     */
    public void setDatas(List<T> list) {
        initDatas(list);
        notifyDataSetChanged();
    }

    public void setDatasWithoutNotifyDataSetChanged(List<T> mDatas) {
        initDatas(mDatas);
    }

    private void initDatas(List<T> list) {
        if (this.mDatas != null) {
            if (null != list) {
                List<T> temp = new ArrayList<>();
                temp.addAll(list);
                mDatas.clear();
                mDatas.addAll(temp);
            } else {
                mDatas.clear();
            }
        } else {
            this.mDatas = list;
        }
    }

    public T getItem(int position) {
        if (position > -1 && null != mDatas && mDatas.size() > position) {
            return mDatas.get(position);
        }
        return null;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(ViewGroup parent, View view, T t, int position);

        boolean onItemLongClick(ViewGroup parent, View view, T t, int position);
    }

    public interface OnItemFoucusChangeListener {
        void onFocusChange(View view, boolean hasFocus, CommonViewHolder viewHolder);
    }
}
