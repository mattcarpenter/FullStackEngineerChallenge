package net.mattcarpenter.performancereview.error;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class Error {
    private String message;
    private String code;
    private String traceId;
    private Date date;

    public Error(ErrorCode code) {
        this.code = code.getCode();
        this.message = code.getDescription();
        this.traceId = UUID.randomUUID().toString();
        this.date = new Date();
    }

    public Error(ErrorCode code, String message) {
        this.code = code.toString();
        this.message = message;
        this.traceId = UUID.randomUUID().toString();
        this.date = new Date();
    }
}
