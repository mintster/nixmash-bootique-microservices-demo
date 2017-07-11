package com.nixmash.web.exceptions;

import java.io.Serializable;

public class RestProcessingException extends Exception implements Serializable {

    private static final long serialVersionUID = -4784131074425744402L;
    private String msg;

    public RestProcessingException() {
        super();
    }

    public RestProcessingException(String msg) {
        this.msg = System.currentTimeMillis()
                + ": " + msg;
    }

    public String getMsg() {
        return msg;
    }


}
