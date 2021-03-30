package com.cfox.camera;

public class ConfigWrapper {

    private ConfigStrategy mConfig = new DefaultConfigStrategy();


    public ConfigWrapper(ConfigStrategy config) {
        if (config != null) {
            mConfig = config;
        }
    }

    public ConfigStrategy getConfig() {
        return mConfig;
    }
}
