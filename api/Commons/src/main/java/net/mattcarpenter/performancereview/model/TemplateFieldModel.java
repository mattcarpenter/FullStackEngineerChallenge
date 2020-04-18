package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * For testing purposes via the admin endpoints only. This is a workaround to enable certain auomation flows in this
 * coding exercise.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateFieldModel {
    private UUID templateId;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;
    private int choiceCount;
    private String prompt;
    private String type;
}
