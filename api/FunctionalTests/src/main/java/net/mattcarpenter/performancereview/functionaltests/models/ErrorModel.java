package net.mattcarpenter.performancereview.functionaltests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorModel {
    private String code;
    private String message;
    private String traceId;
    private Date date;
}
