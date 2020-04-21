package net.mattcarpenter.performancereview.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class UpdateEmployeeRequest {

    private String firstName;

    private String lastName;

    private boolean isAdmin;

    @Email
    private String emailAddress;
}
