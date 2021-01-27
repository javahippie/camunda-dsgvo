package de.ilume.camunda.showcase.testutil;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    /**
     * Builds a Map Java 11 Style, provide keys and values in alternating order
     * @return The constructed Map
     */
    public static Map<String, Object> of(Object... args) {
        Map<String, Object> map = new HashMap<>();
        for(int i = 0; i < args.length; i = i + 2) {
            map.put((String) args[i], args[i+1]);
        }
        return map;
    }
}
