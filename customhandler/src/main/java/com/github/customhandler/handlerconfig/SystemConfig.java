package com.github.customhandler.handlerconfig;

import com.github.customhandler.system.SystemHandler;
import com.github.poseidon.jsbridge.HandlerConfig;
import com.github.poseidon.jsbridge.PoseidonHandler;

import java.util.HashMap;

/**
 * Created by ShuXin on 2018/7/20 10:18
 */
public class SystemConfig implements HandlerConfig{
    /**
     * map.put("service",Handler.class)
     * 此service对应js端的service，需要保持一致
     *
     * @return
     */
    @Override
    public HashMap<String, Class<? extends PoseidonHandler>> getServiceMap() {
        return new HashMap<String, Class<? extends PoseidonHandler>>() {
            {
                put("System", SystemHandler.class);
            }
        };
    }
}
