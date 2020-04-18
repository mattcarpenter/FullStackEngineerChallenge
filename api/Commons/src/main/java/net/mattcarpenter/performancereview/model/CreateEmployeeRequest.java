package net.mattcarpenter.performancereview.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

@Data
@Builder
public class CreateEmployeeRequest {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @NotNull
    private String emailAddress;

    @NotNull
    @Size(min = 10)
    private String password;

    private boolean isAdmin;
}
