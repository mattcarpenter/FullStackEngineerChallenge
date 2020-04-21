package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePerformanceReviewRequest {

    @NotNull
    private UUID reviewee;

    @NotNull
    private String memo;
}
