package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeModel {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private UUID id;

    private boolean isAdmin;
    private List<FeedbackRequestSummaryModel> feedbackRequests;
    private List<PerformanceReviewModel> performanceReviews;
}
