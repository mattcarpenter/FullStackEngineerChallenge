package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CredentialModel {

    private String password;
    private String passwordSalt;
    private String passwordHashAlgorithm;
    private int iterations;
}
