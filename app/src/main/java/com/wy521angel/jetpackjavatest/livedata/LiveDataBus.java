package com.wy521angel.jetpackjavatest.livedata;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LiveDataBus {

    private static LiveDataBus mInstance;
    private static Map<String, MyMutableLiveData> mLiveDatas = new HashMap<>();


    private LiveDataBus() {

    }

    public static LiveDataBus getInstance() {
        if (mInstance == null) {
            synchronized (LiveDataBus.class) {
                if (mInstance == null) {
                    mInstance = new LiveDataBus();
                }
            }
        }
        return mInstance;
    }

    public <T> MyMutableLiveData<T> with(String key, Class<T> type) {
        if (!mLiveDatas.containsKey(key)) {
            mLiveDatas.put(key, new MyMutableLiveData());
        }
        return mLiveDatas.get(key);
    }

    public MyMutableLiveData<Object> with(String target) {
        return with(target, Object.class);
    }

    public void remove(String key) {
        if (mLiveDatas.containsKey(key)) {
            mLiveDatas.remove(key);
        }
    }

    public <T> void post(String key, T t) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            with(key).setValue(t);
        } else {
            with(key).postValue(t);
        }
    }

    public static class MyMutableLiveData<T> extends MutableLiveData<T> {

        //在 observe 被调用的一刻，能够保证 if (observer.mLastVersion >= mVersion) 条件成立
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
            try {
                hook(observer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //处理粘性事件
        public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
        }

        //通过 hook 将 observer.mLastVersion == mVersion
        private void hook(Observer<? super T> observer) throws Exception {

            Class<LiveData> classLiveData = LiveData.class;
            Field fieldObservers = classLiveData.getDeclaredField("mObservers");
            fieldObservers.setAccessible(true);
            Object mObservers = fieldObservers.get(this);
            Class<?> classObservers = mObservers.getClass();

            Method methodGet = classObservers.getDeclaredMethod("get", Object.class);
            methodGet.setAccessible(true);
            Object entry = methodGet.invoke(mObservers, observer);
            Object objectWrapper = ((Map.Entry) entry).getValue();
            Class<?> mObserver = objectWrapper.getClass().getSuperclass();//observer

            Field mLastVersion = mObserver.getDeclaredField("mLastVersion");
            mLastVersion.setAccessible(true);
            Field mVersion = classLiveData.getDeclaredField("mVersion");
            mVersion.setAccessible(true);
            Object mVersionValue = mVersion.get(this);
            mLastVersion.set(objectWrapper, mVersionValue);
        }
    }


}
