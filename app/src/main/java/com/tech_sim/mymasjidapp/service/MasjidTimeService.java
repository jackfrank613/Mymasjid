package com.tech_sim.mymasjidapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MasjidTimeService extends ForeGroundService {

    private Disposable subscribe;

    private TimeChecker timeChecker;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        timeChecker = new TimeChecker(TimeEventBus.getInstance(), this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = super.onStartCommand(intent, flags, startId);

        subscribe = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {

                        if (timeChecker != null) timeChecker.checkMasjidTimeAndBroadcastStream();

                        return aLong;
                    }
                })
                .subscribe();


        return command;
    }

    @Override
    public void onDestroy() {
        subscribe.dispose();
        timeChecker.dispose();
        super.onDestroy();
    }
}
