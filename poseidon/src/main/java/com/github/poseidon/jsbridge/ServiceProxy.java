package com.github.poseidon.jsbridge;

import java.util.HashMap;
import java.util.Map;

public class ServiceProxy {
    private HashMap<String, Class<? extends PoseidonHandler>> map;

    public ServiceProxy() {
        map = new HashMap<>();
    }

    public boolean put(HashMap<String, Class<? extends PoseidonHandler>> serviceMap) {
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
