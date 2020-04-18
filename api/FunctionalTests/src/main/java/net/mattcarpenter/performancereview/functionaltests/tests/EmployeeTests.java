package net.mattcarpenter.performancereview.functionaltests.tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import net.mattcarpenter.performancereview.constants.Constants;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.helpers.EmployeeHelper;
import net.mattcarpenter.performancereview.model.CreateEmployeeRequest;
import net.mattcarpenter.performancereview.model.EmployeeModel;
import net.mattcarpenter.performancereview.model.UpdateEmployeeRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class EmployeeTests {

    private String adminToken;

    @BeforeClass
    public void before() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        adminToken = EmployeeHelper.getAdminToken();
    }

    @Test
    public void createEmployee_happyPath() {
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response response = EmployeeHelper.createTestEmployee(randomEmployee, adminToken);
        assertNewEmployee(randomEmployee, response);
    }

    @Test
    public void createEmployee_noToken() {
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        EmployeeHelper.createTestEmployee(randomEmployee, null).then()
                .body("code", equalTo(TestConstants.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN));
    }

    @Test(enabled = false)
    public void createEmployee_nonAdminToken() {}

    @Test
    public void getEmployee_happyPath() {
        // create test employee and grab id from response
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response createResponse = EmployeeHelper.createTestEmployee(randomEmployee, adminToken);
        EmployeeModel createdEmployee = createResponse.getBody().as(EmployeeModel.class);

        // Log in employee to obtain a token
        Response loginResponse = EmployeeHelper.loginTestEmployee(randomEmployee.getEmailAddress());
        String jwt = loginResponse.getCookie(Constants.TOKEN_COOKIE_NAME);

        // get and validate
        Response getResponse = EmployeeHelper.getEmployee(createdEmployee.getId(), jwt);
        assertNewEmployee(randomEmployee, getResponse);
    }

    @Test
    public void updateEmployee_happyPath() {
        // create test employee and grab id from response
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response createResponse = EmployeeHelper.createTestEmployee(randomEmployee, adminToken);
        EmployeeModel createdEmployee = createResponse.getBody().as(EmployeeModel.class);

        // update employee
        CreateEmployeeRequest randomEmployee2 = EmployeeHelper.makeRandomCreateEmployeeRequest();
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName(randomEmployee2.getFirstName())
                .lastName(randomEmployee2.getLastName())
                .emailAddress(randomEmployee2.getEmailAddress())
                .build();
        EmployeeHelper.updateEmployee(createdEmployee.getId(), updateEmployeeRequest, adminToken).then().assertThat()
                .statusCode(200)
                .body("firstName", equalTo(randomEmployee2.getFirstName()))
                .body("lastName", equalTo(randomEmployee2.getLastName()))
                .body("emailAddress", equalTo(randomEmployee2.getEmailAddress()))
                .body("id", equalTo(createdEmployee.getId().toString()));
    }

    @Test(enabled = false)
    public void updateEmployee_updatesSingleField() {}

    @Test(enabled = false)
    public void updateEmployee_existingEmail_updateFails() {}

    @Test(enabled = false)
    public void updateEmployee_malformedEmail_updateFails() {}

    @Test
    public void createEmployee_emailAlreadyExists() {
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response response = EmployeeHelper.createTestEmployee(randomEmployee, adminToken);
        assertNewEmployee(randomEmployee, response);

        // attempt to create employee using same email address
        CreateEmployeeRequest randomEmployee2 = EmployeeHelper.makeRandomCreateEmployeeRequest();
        randomEmployee2.setEmailAddress(randomEmployee.getEmailAddress());
        EmployeeHelper.createTestEmployee(randomEmployee2, adminToken).then().assertThat()
                .statusCode(400)
                .body("code", equalTo(TestConstants.EMAIL_ALREADY_EXISTS));
    }

    private void assertNewEmployee(CreateEmployeeRequest request, Response response) {
        response.then().assertThat()
                .body("firstName", equalTo(request.getFirstName()))
                .body("lastName", equalTo(request.getLastName()))
                .body("emailAddress", equalTo(request.getEmailAddress()));
    }
}
