package com.bluetooth.cup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;


public class CommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private int mLayoutId;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
    }

    public static CommonViewHolder get(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        CommonViewHolder holder = new CommonViewHolder(itemView);
        holder.mLayoutId = layoutId;
        return holder;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 通过viewId 设置文本
     *
     * @param viewId
     * @param text
     * @return
     */
    public CommonViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 通过viewId 获取控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public CommonViewHolder setTextWithColor(int viewId, CharSequence text, @ColorInt int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        tv.setText(text);
        return this;
    }

    public CommonViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }

    public CommonViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 图片资源Resource
     *
     * @param viewId
     * @param resId
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * 图片资源bitmap
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * 图片资源drawable
     *
     * @param viewId
     * @param drawable
     * @return
     */
    public CommonViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return this;
    }

    public CommonViewHolder setBackgroundColor(int viewId, int color) {
        ImageView imageView = getView(viewId);
        imageView.setBackgroundColor(color);
        return this;
    }

    public CommonViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        ImageView imageView = getView(viewId);
        imageView.setBackgroundColor(backgroundRes);
        return this;
    }

    public CommonViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public CommonViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(itemView.getContext().getResources().getColor(textColorRes));
        return this;
    }

    public CommonViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public CommonViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public CommonViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * 关于触摸事件的
     */
    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonViewHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public CommonViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public CommonViewHolder setFocuseEventListener(int viewId, View.OnKeyListener listener){
        View view = getView(viewId);
        view.setOnKeyListener(listener);
        return this;
    }

    public CommonViewHolder setFocuseChangeListener(int viewId, View.OnFocusChangeListener listener,boolean hasFocus){
        View view = getView(viewId);
        listener.onFocusChange(view,hasFocus);
        return this;
    }
}
