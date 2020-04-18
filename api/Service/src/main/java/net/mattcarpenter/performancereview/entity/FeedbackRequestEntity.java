package net.mattcarpenter.performancereview.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "FeedbackRequest")
@Data
public class FeedbackRequestEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EmployeeEntity reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PerformanceReviewEntity performanceReview;

    @OneToMany(
            mappedBy = "feedbackRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<FeedbackResponseEntity> reviewFeedback = new HashSet<>();

    @ManyToOne
    private FeedbackRequestTemplateEntity template;

    private Date dueOn;
    private Date submittedOn;

    private boolean isDeleted;
}
