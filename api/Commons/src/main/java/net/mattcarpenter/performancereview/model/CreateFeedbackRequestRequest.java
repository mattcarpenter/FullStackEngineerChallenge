package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateFeedbackRequestRequest {

    @NotNull
    private UUID reviewId;

    @NotNull
    private UUID reviewerEmployeeId;

    @NotNull
    private Date dueOn;

    @NotNull
    private UUID feedbackRequestTemplateId;
}
