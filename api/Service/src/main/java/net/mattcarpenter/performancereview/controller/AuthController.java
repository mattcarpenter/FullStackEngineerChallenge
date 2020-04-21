package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.EmployeeModel;
import net.mattcarpenter.performancereview.model.LoginRequest;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.service.AuthService;
import net.mattcarpenter.performancereview.service.EmployeeService;
import net.mattcarpenter.performancereview.utils.CookieUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    AuthService authService;
    EmployeeService employeeService;

    public AuthController(AuthService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;
    }

    @PostMapping(value = "/login")
    public EmployeeModel login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String jwt = authService.login(request.getEmailAddress(), request.getPassword());

        // fetch the employee record using our freshly minted jwt
        Token token = authService.validateJwt(jwt);
        EmployeeEntity entity = employeeService.getEmployee(token);

        // add http-only token cookie to the response
        response.addCookie(CookieUtils.createTokenCookie(jwt));

        return EntityToModelMapper.mapToEmployeeModel(entity);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletResponse response) {
        response.addCookie(CookieUtils.createExpiredCookie());
    }
}
