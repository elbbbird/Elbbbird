package com.elbbbird.android.elbbbird.models;

import java.io.Serializable;

/**
 * Created by zhanghailong-ms on 2015/6/17.
 */
public class Image implements Serializable {
    String hidpi;
    String normal;
    String teaser;

    public String getHidpi() {
        return hidpi;
    }

    public String getNormal() {
        return normal;
    }

    public String getTeaser() {
        return teaser;
    }

    public void setHidpi(String hidpi) {
        this.hidpi = hidpi;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public void setTeaser(String teaser) {
        this.teaser = teaser;
    }
}
