package com.lh.frameproj.ui.fragment1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lh.frameproj.R;
import com.lh.frameproj.ui.BaseFragment;
import com.lh.frameproj.ui.header.RentalsSunHeaderView;
import com.lh.frameproj.ui.main.MainComponent;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
public class Fragment1 extends BaseFragment implements Fragment1Contract.View{

    private static final String TAG = "Fragment1";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.material_style_ptr_frame)
    PtrFrameLayout frame;

    @Inject Fragment1Presenter mFragment1Presenter;

    List<String> data;
    private Fragment1Adapter mFragment1Adapter;
    private LinearLayoutManager mLinearLayoutManager;

    //  0
    @Override
    public int initContentView() {
        return R.layout.fragment_1;
    }

    //  1
    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    //  2
    @Override
    public void getBundle(Bundle bundle) {
        Logger.i("TAG","haha ha");
    }

    //  3
    @Override
    public void initUI(View view) {
        ButterKnife.bind(this, rootView);
        mFragment1Presenter.attachView(this);

        data = new ArrayList<>();
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFragment1Adapter = new Fragment1Adapter(getActivity(), data);
        mRecyclerView.setAdapter(mFragment1Adapter);

        // header
        final RentalsSunHeaderView header = new RentalsSunHeaderView(getActivity());
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setUp(frame);

        frame.setLoadingMinTime(1000);
        frame.setDurationToCloseHeader(1500);
        frame.setHeaderView(header);
        frame.addPtrUIHandler(header);
        frame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                if (mLinearLayoutManager != null) {
                    boolean result=false;
                    if(mLinearLayoutManager.findFirstVisibleItemPosition()==0){
                        final View topChildView = mRecyclerView.getChildAt(0);
                        result=topChildView.getTop()==0;
                    }
                    return result && PtrDefaultHandler
                            .checkContentCanBePulledDown(frame, content, header);
                } else {
                    return PtrDefaultHandler
                            .checkContentCanBePulledDown(frame, content, header);
                }
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                mFragment1Presenter.onThreadReceive();
            }
        });
    }

    @Override
    public void initData() {
        frame.autoRefresh(true);
    }

    @Override
    public void onRefreshCompleted(List<String> datas) {
        data.addAll(datas);
        mFragment1Adapter.notifyDataSetChanged();
        frame.refreshComplete();
    }
}