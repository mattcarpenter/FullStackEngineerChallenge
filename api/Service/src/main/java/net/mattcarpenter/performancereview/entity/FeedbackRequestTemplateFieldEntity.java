package net.mattcarpenter.performancereview.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity(name = "FeedbackRequestTemplateField")
@Data
public class FeedbackRequestTemplateFieldEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    /**
     * Design note: This table contains rows representing various prompts (fields) belonging to a feedback request template.
     * A field may be a freeform textbox or a multiple choice question. To enable storage of any field type in this table,
     * all necessary columns exist in this one table, though only some of them may be populated depending on the field type.
     *
     * To reduce complexity in this PoC, I've chosen to implement a single table inheritance strategy for storing metadata
     * related to feedback request template fields.
     */
    private String type;
    private String prompt;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String choice5;
    private int choiceCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private FeedbackRequestTemplateEntity feedbackRequestTemplate;
}
