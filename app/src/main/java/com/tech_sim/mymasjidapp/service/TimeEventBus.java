package com.tech_sim.mymasjidapp.service;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.subjects.BehaviorSubject;

public class TimeEventBus {


    private BehaviorSubject<MasjidTime> bus = BehaviorSubject.create();

    private TimeEventBus(){

    }

    private static TimeEventBus instance;

    public synchronized static TimeEventBus getInstance() {
        if(instance == null){
            instance = new TimeEventBus();
        }

        return  instance;
    }


    public void brodcast(MasjidTime time){
        bus.onNext(time);
    }

    public Observable<MasjidTime> listen(){
        return bus.map(new Function<MasjidTime, MasjidTime>() {
            @Override
            public MasjidTime apply(MasjidTime time) throws Exception {
                return time;
            }
        });
    }

}
