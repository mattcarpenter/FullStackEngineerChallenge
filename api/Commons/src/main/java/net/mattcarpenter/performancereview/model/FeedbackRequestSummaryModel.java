package net.mattcarpenter.performancereview.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class FeedbackRequestSummaryModel {
    private UUID id;
    private String reviewer;
    private Date dueOn;
    private Date submittedOn;
    private String templateName;
}
