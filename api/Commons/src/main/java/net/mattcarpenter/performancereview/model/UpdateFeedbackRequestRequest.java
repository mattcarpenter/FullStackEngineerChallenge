package net.mattcarpenter.performancereview.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UpdateFeedbackRequestRequest {
    private List<QuestionModel> questions;
    private Date dueOn;
}
