package com.github.poseidon.jsbridge;

import android.app.Activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ShuXin on 2018/7/17 15:40
 */
public class PoseidonInterfaceImpl implements PoseidonInterface {
    protected Activity activity;
    protected ExecutorService threadPool;

    public PoseidonInterfaceImpl(Activity activity) {
        this(activity, Executors.newCachedThreadPool());
    }

    public PoseidonInterfaceImpl(Activity activity, ExecutorService threadPool) {
        this.activity = activity;
        this.threadPool = threadPool;
    }

    @Override
    public Activity getActivity() {
        return activity;
    }

    @Override
    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
