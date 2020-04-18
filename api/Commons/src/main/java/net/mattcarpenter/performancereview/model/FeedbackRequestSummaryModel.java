package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestSummaryModel {
    private UUID id;
    private EmployeePublicDataModel reviewer;
    private EmployeePublicDataModel reviewee;
    private Date dueOn;
    private Date submittedOn;
    private String templateName;
}
