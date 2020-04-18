package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePublicDataModel {
    private String firstName;
    private String lastName;
}
