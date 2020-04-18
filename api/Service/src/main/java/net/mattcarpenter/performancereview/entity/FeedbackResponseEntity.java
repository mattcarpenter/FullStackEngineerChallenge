package net.mattcarpenter.performancereview.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "FeedbackResponse")
@Data
public class FeedbackResponseEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private FeedbackRequestEntity feedbackRequest;

    @ManyToOne
    @Access(AccessType.PROPERTY)
    private FeedbackRequestTemplateFieldEntity templateField;

    private String response;
}
