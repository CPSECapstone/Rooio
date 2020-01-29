package com.rooio.repairs;

import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import java.util.HashMap;

public class JsonRequest {

    private String url;
    HashMap<String,Object> params;
    private final Function<Object, Void> responseFunc;
    private final Function<String, Void> errorFunc;
    private final boolean headersFlag;
    private boolean test;

    public JsonRequest(boolean test, String url, @Nullable HashMap<String, Object> params, Function<Object, Void> responseFunc,
                       Function<String, Void> errorFunc, boolean headersFlag) {

        this.test = test;
        this.url = url;
        this.params = params;
        this.responseFunc = responseFunc;
        this.errorFunc = errorFunc;
        this.headersFlag = headersFlag;
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public Function<Object, Void> getResponseFunc() {
        return responseFunc;
    }

    public Function<String, Void> getErrorFunc() {
        return errorFunc;
    }

    public boolean getHeadersFlag() {
        return headersFlag;
    }

    public boolean isTest() {
        return test;
    }
}
