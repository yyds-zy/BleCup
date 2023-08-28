package com.bluetooth.cup.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bluetooth.cup.R;
import com.bluetooth.cup.adapter.RecycleViewAdapter;
import com.bluetooth.cup.model.CupRecordData;

import com.bluetooth.cup.model.RecordListBean;
import com.bluetooth.cup.ui.BaseFragment;
import com.bluetooth.cup.util.CupMessageManager;
import com.bluetooth.cup.util.MiscUtil;
import com.bluetooth.cup.util.NotifyUtil;
import com.orm.SugarRecord;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;


public class MessageFragment extends BaseFragment implements CupMessageManager.OnCupStateListener {
    RecyclerView rvRegisterList;
    RecycleViewAdapter recycleViewAdapter;
    List<RecordListBean> cupRecordDataList = new ArrayList<>();

    private int totalSize = 0;
    private int currentPager = 1;
    private int pageSize = 10;
    private int recycleViewState = 0;

    private List<CupRecordData> mTagList = new ArrayList<>();
    RefreshLayout mRefreshLayout;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_layout_msg;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        CupMessageManager.getInstance().setOnCupStateListener(this);
        recycleViewAdapter = new RecycleViewAdapter(cupRecordDataList);
        rvRegisterList = view.findViewById(R.id.rvRegisterList);
        mRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        rvRegisterList.setAdapter(recycleViewAdapter);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                List<RecordListBean> recordListBeans = initData();
                Message message = new Message();
                message.what = 1 ;
                message.obj = recordListBeans ;
                mHandler.sendMessageDelayed(message,2000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                List<RecordListBean> data = initData();
                Message message = new Message();
                message.what = 2 ;
                message.obj = data ;
                mHandler.sendMessageDelayed(message,2000);
            }
        });

        List<RecordListBean> data = initData();
        recycleViewAdapter.refresh(data);
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 1:         //刷新加载
                    List<RecordListBean> mList  = (List<RecordListBean>) msg.obj;
                    mRefreshLayout.finishRefresh(true);
                    recycleViewAdapter.refresh(mList);
                    break;
                case 2:         //加载更多
                    List<RecordListBean> mLoadMoreDatas = (List<RecordListBean>) msg.obj;
                    mRefreshLayout.finishLoadMore(true);
                    recycleViewAdapter.refresh(mLoadMoreDatas);
                    break;
                case 3:         //添加数据
                    List<RecordListBean> data = initData();
                    recycleViewAdapter.refresh(data);
                    break;
            }
            return false;
        }
    });

    private List<RecordListBean> initData() {
        cupRecordDataList.clear();
        List<RecordListBean> dataList = RecordListBean.findWithQuery(RecordListBean.class,"SELECT * FROM RECORD_LIST_BEAN LIMIT ? OFFSET ?;","20","0");
        return dataList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //1表示开盖  0表示关盖子
    @Override
    public void state(String data) {
        if (TextUtils.isEmpty(data)) return;
        Log.d("xuezhiyuan","-------------------------"+data);
        handleData(false,data);
    }

    private void handleData(boolean isHistory,String data) {
        String[] split = data.split(":");
        String cupState = split[0];
        String electricity = split[2].substring(0,split[2].length()-1);
        if (!isHistory) {
            if (cupState == "0") {
                NotifyUtil.getInstance().sendNotify(getContext(),"关闭水杯", 0);
            } else {
                NotifyUtil.getInstance().sendNotify(getContext(),"开启水杯", 1);
            }
        }
        CupRecordData cupRecordData = new CupRecordData();
        if (mTagList.size() == 0) {
            if ("1".equals(cupState)) { //开启杯子
                String openTime = split[1];
                cupRecordData.setOpenTime(openTime);
                cupRecordData.setWeeks(MiscUtil.getDay(openTime));
                mTagList.add(cupRecordData);
            }
        } else  if (mTagList.size() == 1) {
            if ("0".equals(cupState)) { //关闭杯子
                String closeTime = split[1];
                String openTime = mTagList.get(0).getOpenTime();
                if (Long.parseLong(openTime) <= Long.parseLong(closeTime)) {
                    RecordListBean recordListBean = new RecordListBean(closeTime, openTime, MiscUtil.getDay(closeTime), electricity);
                    SugarRecord.save(recordListBean);
                    Message message = new Message();
                    message.what = 3 ;
                    mHandler.sendMessageDelayed(message,2000);
                }
                mTagList.clear();
            }
        }

    }
}
