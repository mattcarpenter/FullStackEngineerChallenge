package net.mattcarpenter.performancereview.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CreatePerformanceReviewRequest {

    @NotNull
    private UUID reviewee;
}
