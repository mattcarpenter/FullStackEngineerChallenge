package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionModel {
    private UUID fieldId;
    private String type;
    private List<String> choices;
    private String prompt;
    private String response;
}
