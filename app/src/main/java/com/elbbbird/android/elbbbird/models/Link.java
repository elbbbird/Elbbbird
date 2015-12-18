package com.elbbbird.android.elbbbird.models;

import java.io.Serializable;

/**
 * Created by zhanghailong-ms on 2015/6/17.
 */
public class Link implements Serializable {
    String web;
    String twitter;

    public String getWeb() {
        return web;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
