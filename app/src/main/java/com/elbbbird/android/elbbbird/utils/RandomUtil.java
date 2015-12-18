package com.elbbbird.android.elbbbird.utils;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by zhanghailong-ms on 2015/9/23.
 */
public class RandomUtil {

    private static HashMap<Long, Object> CACHE = new HashMap<Long, Object>();

    /**
     * 通过cache保证不会重复
     *
     * @return
     */
    public static synchronized long getLong() {
        long id = new Random().nextLong();
        while (CACHE.containsKey(id)) {
            id = new Random().nextLong();
        }
        CACHE.put(id, 0);
        return id;
    }
}
