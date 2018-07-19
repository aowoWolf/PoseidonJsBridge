package com.github.poseidon.jsbridge;

import java.util.HashMap;

public interface HandlerConfig {
    /**
     * map.put("service",Handler.class)
     * 此service对应js端的service，需要保持一致
     *
     * @return
     */
    HashMap<String, Class<? extends PoseidonHandler>> getServiceMap();
}
