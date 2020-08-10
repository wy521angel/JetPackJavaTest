package com.wy521angel.jetpackjavatest.livedata;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class LiveDataBusBeta {

    private final Map<String, MutableLiveData<Object>> bus;

    public LiveDataBusBeta() {
        bus = new HashMap<>();
    }

    private static class SingleHolder {
        private static final LiveDataBusBeta DATA_BUS = new LiveDataBusBeta();
    }

    public static LiveDataBusBeta getInstance() {
        return SingleHolder.DATA_BUS;
    }

    public <T> MutableLiveData<T> with(String key, Class<T> type) {
        if (!bus.containsKey(key)) {
            bus.put(key, new MutableLiveData<Object>());
        }
        return (MutableLiveData<T>) bus.get(key);
    }

    public MutableLiveData<Object> with(String target) {
        return with(target, Object.class);
    }

    public void remove(String key) {
        if (bus.containsKey(key)) {
            bus.remove(key);
        }
    }
}
