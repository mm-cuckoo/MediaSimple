package com.cfox.camera;

import com.cfox.camera.utils.EsResult;

public class EsException extends Exception {
    public final int errorCode;
    public final EsResult result;

    public EsException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.result = null;
    }

    public EsException(String errorMsg, int errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.result = null;
    }

    public EsException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.result = null;
    }

    public EsException(String errorMsg, int errorCode, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.result = null;
    }

    public EsException(String errorMsg, int errorCode, Throwable throwable, EsResult result) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.result = result;
    }

    public EsException(String errorMsg, EsResult result) {
        super(errorMsg);
        this.result = result;
        this.errorCode = 0;
    }

    public EsException(String errorMsg, int errorCode, EsResult result) {
        super(errorMsg);
        this.result = result;
        this.errorCode = errorCode;
    }
}
