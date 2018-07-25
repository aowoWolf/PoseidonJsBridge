package com.github.poseidon.jsbridge;

import java.util.HashMap;

public interface HandlerConfig {
    /**
     * map.put("service",Handler.class)
     * 此service对应js端的service，需要保持一致
     *
     * @return 需要注册的handler的map
     */
    HashMap<String, Class<? extends PoseidonHandler>> getServiceMap();
}
