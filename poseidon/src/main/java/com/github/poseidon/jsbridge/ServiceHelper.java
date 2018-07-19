package com.github.poseidon.jsbridge;

import java.util.HashMap;
import java.util.Map;

/**
 * 每个handler的service和class的配置信息集合
 */
public class ServiceHelper {
    private HashMap<String, Class<? extends PoseidonHandler>> map;

    public ServiceHelper() {
        map = new HashMap<>();
    }

    public boolean put(HandlerConfig handlerconfig) {
        HashMap<String, Class<? extends PoseidonHandler>> serviceMap = handlerconfig.getServiceMap();
        if (serviceMap.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, Class<? extends PoseidonHandler>> entry : serviceMap.entrySet()) {
            String handler = entry.getKey();

            if (map.containsKey(handler)) {
                if (!map.get(handler).equals(entry.getValue())) {
                    throw new IllegalArgumentException("Please check if the " + handler + " handler is reused.");
                }
            }
            map.put(handler, entry.getValue());

        }

        return true;
    }

    public HashMap<String, Class<? extends PoseidonHandler>> getMap() {
        return map;
    }

}
