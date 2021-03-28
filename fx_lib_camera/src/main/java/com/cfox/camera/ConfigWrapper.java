package com.cfox.camera;

public class ConfigWrapper implements IConfigWrapper {

    private ConfigStrategy mConfig = new DefaultConfigStrategy();


    public ConfigWrapper(ConfigStrategy config) {
        if (config != null) {
            mConfig = config;
        }
    }

    @Override
    public ConfigStrategy getConfig() {
        return mConfig;
    }
}
