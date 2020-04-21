package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.*;
import net.mattcarpenter.performancereview.service.AuthService;
import net.mattcarpenter.performancereview.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private AuthService authService;

    public EmployeeController(EmployeeService employeeService, AuthService authService) {
        this.employeeService = employeeService;
        this.authService = authService;
    }

    @GetMapping()
    public EmployeeListResponse getAllEmployees() {
        List<EmployeeModel> employees = employeeService.getEmployees()
                .stream()
                .map(EntityToModelMapper::mapToEmployeeModel)
                .collect(Collectors.toList());

        return new EmployeeListResponse(employees);
    }

    @GetMapping(value = "/{employeeId:.+}")
    public EmployeeModel getEmployee(@PathVariable UUID employeeId) {
        Token token = authService.loadTokenFromSecurityContext();
        EmployeeEntity employeeEntity = employeeService.getEmployee(employeeId, token);
        return EntityToModelMapper.mapToEmployeeModel(employeeEntity);
    }

    @DeleteMapping(value = "/{employeeId:.+}")
    public void deleteEmployee(@PathVariable UUID employeeId) {
        employeeService.deactivateEmployee(employeeId);
    }

    @PostMapping(value = "/{employeeId:.+}")
    public EmployeeModel updateEmployee(@PathVariable UUID employeeId, @RequestBody UpdateEmployeeRequest request) {
        Token token = authService.loadTokenFromSecurityContext();
        EmployeeEntity entity = employeeService.updateEmployee(employeeId, request.getFirstName(), request.getLastName(),
                request.getEmailAddress(), request.isAdmin(), token);
        return EntityToModelMapper.mapToEmployeeModel(entity);
    }

    @PostMapping
    public EmployeeModel createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        Token token = authService.loadTokenFromSecurityContext();
        EmployeeEntity entity =  employeeService.createEmployee(request.getFirstName(), request.getLastName(), request.getEmailAddress(),
                request.getPassword(), request.isAdmin(), token);
        return EntityToModelMapper.mapToEmployeeModel(entity);
    }
}
