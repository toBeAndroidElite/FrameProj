package com.xjgj.mall.components.okhttp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by WE-WIN-027 on 2016/9/2.
 *
 * @des ${TODO}
 */
public class AddCookiesInterceptor implements Interceptor {
    private static final String TAG = "AddCookiesInterceptor";
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
        Observable.just(sharedPreferences.getString("cookie", ""))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String cookie) throws Exception {
                        //添加cookie
                        builder.addHeader("Cookie", cookie);
                    }
                });
        return chain.proceed(builder.build());
    }
}