package net.mattcarpenter.performancereview.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "PerformanceReview")
@Data
public class PerformanceReviewEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private EmployeeEntity revieweeEmployee;

    @OneToMany(
            mappedBy = "performanceReview",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<FeedbackRequestEntity> reviewFeedback = new HashSet<>();

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private boolean isDeleted;

    @OneToOne(fetch = FetchType.LAZY)
    private EmployeeEntity createdBy;
}
