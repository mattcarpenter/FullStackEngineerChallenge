package net.mattcarpenter.performancereview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Employee")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @OneToMany(
            mappedBy = "revieweeEmployee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<PerformanceReviewEntity> reviews = new HashSet<>();

    @OneToOne(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private CredentialEntity credential;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String emailAddress;

    private UUID createdBy;

    private boolean isAdmin;
    private boolean isActive;
}
