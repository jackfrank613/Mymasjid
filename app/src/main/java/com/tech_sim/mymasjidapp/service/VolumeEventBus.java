package com.tech_sim.mymasjidapp.service;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class VolumeEventBus {


    private BehaviorSubject<Boolean> bus = BehaviorSubject.create();

    private VolumeEventBus(){

    }

    private static VolumeEventBus instance;

    public synchronized static VolumeEventBus getInstance() {
        if(instance == null){
            instance = new VolumeEventBus();
        }

        return  instance;
    }


    public Boolean lastBrodcastValue(){
        return bus.getValue() == null ? true:bus.getValue();
    }

    public void brodcast(Boolean time){
        bus.onNext(time);
    }

    public Observable<Boolean> listen(){
        return bus.map(new Function<Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean time) throws Exception {
                return time;
            }
        });
    }

}
