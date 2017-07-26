package com.github.baby.owspace.presenter;


import com.github.baby.owspace.app.OwspaceApplication;
import com.github.baby.owspace.model.api.ApiService;
import com.github.baby.owspace.model.entity.SplashEntity;
import com.github.baby.owspace.util.NetUtil;
import com.github.baby.owspace.util.OkHttpImageDownloader;
import com.github.baby.owspace.util.TimeUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Mr.Yangxiufeng
 * DATE 2016/7/22
 * owspace
 */
@SuppressWarnings("ALL")
public class SplashPresenter implements SplashContract.Presenter {

    private ApiService apiService;
    private SplashContract.View rootView;

    @Inject
    SplashPresenter(SplashContract.View view, ApiService apiService) {
        this.rootView = view;
        this.apiService = apiService;
    }

    @Override
    public void getSplash(String deviceId) {
        String client = "android";
        String version = "1.3.0";
        Long time = TimeUtil.getCurrentSeconds();
        apiService.getSplash(client, version, time, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<SplashEntity>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("load splash onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        rootView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(SplashEntity splashEntity) {
                        if (NetUtil.isWifi(OwspaceApplication.getInstance().getApplicationContext())) {
                            if (splashEntity != null) {
                                List<String> imgs = splashEntity.getImages();
                                if (imgs == null || imgs.size() == 0) {
                                    return;
                                }
                                for (String url : imgs) {
                                    OkHttpImageDownloader.download(url);
                                }
                            }
                        } else {
                            rootView.showError("不是WIFI环境,就不去下载图片了");
                        }
                    }
                });
    }
}
