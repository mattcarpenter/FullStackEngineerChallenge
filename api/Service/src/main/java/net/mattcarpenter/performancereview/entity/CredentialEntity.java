package net.mattcarpenter.performancereview.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Credential")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CredentialEntity {

    @Id
    @Type(type = "pg-uuid")
    private UUID id = UUID.randomUUID();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    private String password;
    private String passwordSalt;
    private String passwordHashAlgorithm;
    private int iterations;
}
