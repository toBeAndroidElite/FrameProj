package com.lh.frameproj.ui.main;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;

import com.lh.frameproj.R;
import com.lh.frameproj.injector.PerActivity;
import com.lh.frameproj.ui.fragment1.Fragment1;
import com.lh.frameproj.ui.fragment2.Fragment2;
import com.lh.frameproj.ui.fragment3.Fragment3;
import com.lh.frameproj.ui.fragment4.Fragment4;

import javax.inject.Inject;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@PerActivity
public class MainPresenter implements MainContract.Presenter {

    private Context mContext;
    private MainContract.View mMainView;

    @Inject
    public MainPresenter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void attachView(@NonNull MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void detachView() {
        mMainView = null;
    }

    @Override
    public void onTabClick(int position) {

        Fragment mFragment = null;
        switch (position) {

            case 0:
                mMainView.setTitle(mContext.getResources().getString(R.string.MainActivity_title_news));
                mFragment = new Fragment1();
                break;
            case 1:
                mMainView.setTitle(mContext.getResources().getString(R.string.MainActivity_title_photo));
                mFragment = new Fragment2();
                break;
            case 2:
                mMainView.setTitle(mContext.getResources().getString(R.string.MainActivity_title_video));
                mFragment = new Fragment3();
                break;
            case 3:
                mMainView.setTitle(mContext.getResources().getString(R.string.MainActivity_title_about));
                mFragment = new Fragment4();
                break;

        }
        mMainView.showFragment(mFragment);

    }
}