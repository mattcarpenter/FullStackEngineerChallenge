package net.mattcarpenter.performancereview.entity;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "FeedbackRequestTemplate")
@Data
public class FeedbackRequestTemplateEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    private String name;

    @OneToMany(
            mappedBy = "feedbackRequestTemplate",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<FeedbackRequestTemplateFieldEntity> fields;
}
