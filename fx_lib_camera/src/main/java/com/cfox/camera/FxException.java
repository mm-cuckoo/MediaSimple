package com.cfox.camera;

import com.cfox.camera.utils.FxResult;

public class FxException extends Exception {
    public final int errorCode;
    public final FxResult result;

    public FxException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.result = null;
    }

    public FxException(String errorMsg, int errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.result = null;
    }

    public FxException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.result = null;
    }

    public FxException(String errorMsg, int errorCode, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.result = null;
    }

    public FxException(String errorMsg, int errorCode, Throwable throwable, FxResult result) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.result = result;
    }

    public FxException(String errorMsg, FxResult result) {
        super(errorMsg);
        this.result = result;
        this.errorCode = 0;
    }

    public FxException(String errorMsg, int errorCode, FxResult result) {
        super(errorMsg);
        this.result = result;
        this.errorCode = errorCode;
    }
}
