package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.CreateEmployeeRequest;
import net.mattcarpenter.performancereview.model.EmployeeModel;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
public class AdminController {

    EmployeeService employeeService;

    public AdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(path = "/employees")
    public EmployeeModel createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {

        /**
         * BAD THING ALERT
         *
         * This is an administrative endpoint intended to be used by automation, systems engineers, etc...
         * In a production application, these admin endpoints would be protected by some sort of authentication strategy
         * but in order to reduce this coding exercises scope, I've opted not to implement any sort of authentication on
         * this endpoint.
         */

        Token fakeToken = new Token(null, true, 30, null);
        EmployeeEntity entity =  employeeService.createEmployee(request.getFirstName(), request.getLastName(), request.getEmailAddress(),
                request.getPassword(), request.isAdmin(), fakeToken);

        return EntityToModelMapper.mapToEmployeeModel(entity);
    }
}
