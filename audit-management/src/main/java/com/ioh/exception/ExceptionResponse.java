package com.ioh.exception;

import java.util.Date;

public class ExceptionResponse {
    private long timestamp;
    private String message;
    private String details;

    private String code;

    public ExceptionResponse(Date timestamp, String message, String details, String code) {
        super();
        this.timestamp = timestamp.getTime();
        this.message = message;
        this.details = details;
        this.code = code;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public String getCode() {
        return code;
    }
}
