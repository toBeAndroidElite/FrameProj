package com.xjgj.mall.ui.businesslicence;

import android.support.annotation.NonNull;

import com.android.frameproj.library.util.ToastUtil;
import com.xjgj.mall.api.common.CommonApi;
import com.xjgj.mall.bean.HttpResult;
import com.xjgj.mall.bean.PhotoUploadEntity;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by we-win on 2017/7/31.
 */

public class BusinessLicencePresenter implements BusinessLicenceContract.Presenter {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private CommonApi mCommonApi;

    private BusinessLicenceContract.View mView;

    @Inject
    public BusinessLicencePresenter(CommonApi commonApi) {
        mCommonApi = commonApi;
    }

    @Override
    public void photoUpload(String photoPath, int type) {
        if(photoPath == null || photoPath.equals("")){
            ToastUtil.showToast("请选择营业执照");
            return;
        }
        mView.showLoading();
        disposables.add(mCommonApi.photoUpload(new File(photoPath),type)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<PhotoUploadEntity>, ObservableSource<PhotoUploadEntity>>() {
                    @Override
                    public ObservableSource<PhotoUploadEntity> apply(@io.reactivex.annotations.NonNull HttpResult<PhotoUploadEntity> uploadEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(uploadEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mView.hideLoading();
                    }
                }).subscribe(new Consumer<PhotoUploadEntity>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull PhotoUploadEntity photoUploadEntity) throws Exception {
                        mView.photoUploadSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void photoQuery() {
        disposables.add(mCommonApi.photoQuery(2)
                .debounce(800, TimeUnit.MILLISECONDS)
                .flatMap(new Function<HttpResult<List<PhotoUploadEntity>>, ObservableSource<List<PhotoUploadEntity>>>() {
                    @Override
                    public ObservableSource<List<PhotoUploadEntity>> apply(@io.reactivex.annotations.NonNull HttpResult<List<PhotoUploadEntity>> realNameQueryEntityHttpResult) throws Exception {
                        return CommonApi.flatResponse(realNameQueryEntityHttpResult);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PhotoUploadEntity>>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull List<PhotoUploadEntity> list) throws Exception {
                        if (list.size()!=0) {
                            mView.photoQuerySuccess(list.get(0));
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        mView.onError(throwable);
                    }
                }));
    }

    @Override
    public void attachView(@NonNull BusinessLicenceContract.View view) {
        mView = view;
    }

    @Override
    public void detachView() {
        disposables.clear();
        mView = null;
    }

}
