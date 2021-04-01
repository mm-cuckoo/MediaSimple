package com.cfox.camera;

public class EsException extends Exception {
    public final int errorCode;
    public final EsParams esParams;

    public EsException(String errorMsg) {
        super(errorMsg);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.esParams = null;
    }

    public EsException(String errorMsg, int errorCode) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.esParams = null;
    }

    public EsException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = 0;//EVENT_VALUE_UNDEFINED;
        this.esParams = null;
    }

    public EsException(String errorMsg, int errorCode, Throwable throwable) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.esParams = null;
    }

    public EsException(String errorMsg, int errorCode, Throwable throwable, EsParams esParams) {
        super(errorMsg, throwable);
        this.errorCode = errorCode;
        this.esParams = esParams;
    }

    public EsException(String errorMsg, EsParams esParams) {
        super(errorMsg);
        this.esParams = esParams;
        this.errorCode = 0;
    }

    public EsException(String errorMsg, int errorCode, EsParams esParams) {
        super(errorMsg);
        this.esParams = esParams;
        this.errorCode = errorCode;
    }
}
