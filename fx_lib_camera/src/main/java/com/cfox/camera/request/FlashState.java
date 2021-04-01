package com.cfox.camera.request;

import com.cfox.camera.EsParams;

public enum FlashState {
    ON(EsParams.Value.FLASH_STATE.ON),
    AUTO(EsParams.Value.FLASH_STATE.AUTO),
    OFF(EsParams.Value.FLASH_STATE.OFF);

    private final int mFlashState;
    FlashState(int state) {
        mFlashState = state;
    }

    public int getState() {
        return mFlashState;
    }
}
