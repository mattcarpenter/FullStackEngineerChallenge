package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PerformanceReviewModel {
    private UUID id;
    private UUID revieweeEmployeeId;
    private UUID createdByEmployeeId;
    private Date createdAt;
    private List<FeedbackRequestSummaryModel> feedbackRequests;
}