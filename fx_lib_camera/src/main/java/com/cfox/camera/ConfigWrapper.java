package com.cfox.camera;

public class ConfigWrapper implements IConfigWrapper {

    private IConfig mConfig;

    @Override
    public void setConfig(IConfig config) {
        this.mConfig = config;
    }

    @Override
    public IConfig getConfig() {
        if (mConfig == null) {
            mConfig = new DefaultConfig();
        }
        return mConfig;
    }
}
