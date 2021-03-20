package com.cfox.camera;

public class ConfigWrapper implements IConfigWrapper {

    private IConfig mConfig = new DefaultConfig();


    public ConfigWrapper(IConfig config) {
        if (config != null) {
            mConfig = config;
        }
    }

    @Override
    public IConfig getConfig() {
        return mConfig;
    }
}
