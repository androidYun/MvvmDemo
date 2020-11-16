package com.xhs.mlecg;

public class GlobalConfig {
    private static GlobalConfig instance = null;
    private String url = "";

    private GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        if (instance == null) {
            instance = new GlobalConfig();
        }
        return instance;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }
}
