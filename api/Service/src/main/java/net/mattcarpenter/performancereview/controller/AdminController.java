package net.mattcarpenter.performancereview.controller;

import io.swagger.annotations.Api;
import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.*;
import net.mattcarpenter.performancereview.service.EmployeeService;
import net.mattcarpenter.performancereview.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * BAD THING ALERT
 *
 * These are administrative endpoints intended to be used by automation, systems engineers, etc...
 * In a production application, these admin endpoints would be protected by some sort of authentication strategy
 * but in order to reduce this coding exercises scope, I've opted to leave these endpoints unprotected.
 */

@RestController
@RequestMapping(path = "/admin")
@Api(description = "Administrative endpoints used by automated functional tests only. To reduce scope, no authentication strategy has been implemented on these endpoints.")
public class AdminController {

    EmployeeService employeeService;
    FeedbackRequestService feedbackRequestService;

    public AdminController(EmployeeService employeeService, FeedbackRequestService feedbackRequestService) {
        this.employeeService = employeeService;
        this.feedbackRequestService = feedbackRequestService;
    }

    @PostMapping(path = "/employees")
    public EmployeeModel createEmployee(@RequestBody @Valid CreateEmployeeRequest request) {
        Token fakeToken = new Token(null, true, 30, null);
        EmployeeEntity entity =  employeeService.createEmployee(request.getFirstName(), request.getLastName(), request.getEmailAddress(),
                request.getPassword(), request.isAdmin(), fakeToken);

        return EntityToModelMapper.mapToEmployeeModel(entity);
    }

    @PostMapping(path = "/templates")
    public String createTemplate(@RequestBody TemplateModel request) {
        return feedbackRequestService.createFeedbackRequestTemplate(request.getName()).toString();
    }

    @PostMapping(path = "/template-fields")
    public String createTemplateField(@RequestBody TemplateFieldModel request) {
        return feedbackRequestService.createFeedbackRequestTemplateField(request).toString();
    }
}
