package net.mattcarpenter.performancereview.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmployeeListResponse {
    List<EmployeeModel> employees;
}
