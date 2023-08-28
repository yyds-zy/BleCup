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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{

    private List<RecordListBean> mCupStateDataList;

    public RecycleViewAdapter(List<RecordListBean> fileDataList){
        mCupStateDataList = fileDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cup_state, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecordListBean dataBean = mCupStateDataList.get(position);
        holder.mOpenCupTime.setText(MiscUtil.getCurrentTime(dataBean.getOpenTime()));
        holder.mCloseCupTime.setText(MiscUtil.getCurrentTime(dataBean.getCloseTime()));
        holder.mWeek.setText(dataBean.getWeeks());
    }

    @Override
    public int getItemCount() {
        if (mCupStateDataList == null) return 0;
        return mCupStateDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mWeek, mOpenCupTime, mCloseCupTime;
        public ViewHolder(View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.week_tv);
            mOpenCupTime = itemView.findViewById(R.id.open_cup_time_tv);
            mCloseCupTime = itemView.findViewById(R.id.close_cup_time_tv);
        }
    }

    public void refresh(List<RecordListBean> addList) {
        //增加数据
        int position = mCupStateDataList.size();
        mCupStateDataList.addAll(position, addList);
        notifyDataSetChanged();
    }
}