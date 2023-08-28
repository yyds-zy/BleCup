package com.bluetooth.cup.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluetooth.cup.R;
import com.bluetooth.cup.model.RecordListBean;
import com.bluetooth.cup.util.MiscUtil;

import java.util.List;

public class RecorderAdapter extends RecyclerView.Adapter<RecorderAdapter.ViewHolder>{

    private List<RecordListBean> mRecordBeanList;

    public RecorderAdapter(List<RecordListBean> recorderBeans){
        mRecordBeanList = recorderBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recorder_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordListBean dataBean = mRecordBeanList.get(position);
        holder.mWeek.setText(dataBean.getWeeks());
        holder.mOpenTimeTv.setText(MiscUtil.getCurrentTime(dataBean.getOpenTime()));
        holder.mCloseTimeTv.setText(MiscUtil.getCurrentTime(dataBean.getCloseTime()));
    }

    @Override
    public int getItemCount() {
        if (mRecordBeanList == null) return 0;
        return mRecordBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mWeek, mOpenTimeTv, mCloseTimeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.week_tv);
            mOpenTimeTv = itemView.findViewById(R.id.open_cup_time_tv);
            mCloseTimeTv = itemView.findViewById(R.id.close_cup_time_tv);
        }
    }
}
