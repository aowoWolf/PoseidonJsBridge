package com.github.poseidon.jsbridge;

import android.app.Activity;

import java.util.concurrent.ExecutorService;

/**
 * Created by ShuXin on 2018/7/17 15:39
 */
public interface PoseidonInterface {
    Activity getActivity();
    ExecutorService getThreadPool();
}
