package com.github.customhandler.handlerconfig;

import com.github.customhandler.test.CustomHandler;
import com.github.poseidon.jsbridge.HandlerConfig;
import com.github.poseidon.jsbridge.PoseidonHandler;

import java.util.HashMap;

/**
 * Created by ShuXin on 2018/7/16 16:51
 */
public class CustomConfig implements HandlerConfig {
    /**
     * map.put("service",Service.class)
     *
     * @return
     */
    @Override
    public HashMap<String, Class<? extends PoseidonHandler>> getServiceMap() {
        return new HashMap<String, Class<? extends PoseidonHandler>>() {
            {
                put("customservice", CustomHandler.class);
            }
        };
    }
}
