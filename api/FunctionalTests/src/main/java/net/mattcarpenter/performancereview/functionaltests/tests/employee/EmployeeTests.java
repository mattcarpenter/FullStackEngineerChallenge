package net.mattcarpenter.performancereview.functionaltests.tests.employee;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.helpers.EmployeeHelper;
import net.mattcarpenter.performancereview.model.CreateEmployeeRequest;
import net.mattcarpenter.performancereview.model.EmployeeModel;
import net.mattcarpenter.performancereview.model.UpdateEmployeeRequest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

public class EmployeeTests {

    @BeforeClass
    public void before() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void createEmployee_happyPath() {
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response response = EmployeeHelper.createTestEmployee(randomEmployee);
        assertNewEmployee(randomEmployee, response);
    }

    @Test
    public void getEmployee_happyPath() {
        // create test employee and grab id from response
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response createResponse = EmployeeHelper.createTestEmployee(randomEmployee);
        EmployeeModel createdEmployee = createResponse.getBody().as(EmployeeModel.class);

        // get and validate
        Response getResponse = EmployeeHelper.getEmployee(createdEmployee.getId());
        assertNewEmployee(randomEmployee, getResponse);
    }

    @Test
    public void updateEmployee_happyPath() {
        // create test employee and grab id from response
        CreateEmployeeRequest randomEmployee = EmployeeHelper.makeRandomCreateEmployeeRequest();
        Response createResponse = EmployeeHelper.createTestEmployee(randomEmployee);
        EmployeeModel createdEmployee = createResponse.getBody().as(EmployeeModel.class);

        // update employee
        CreateEmployeeRequest randomEmployee2 = EmployeeHelper.makeRandomCreateEmployeeRequest();
        UpdateEmployeeRequest updateEmployeeRequest = UpdateEmployeeRequest.builder()
                .firstName(randomEmployee2.getFirstName())
                .lastName(randomEmployee2.getLastName())
                .emailAddress(randomEmployee2.getEmailAddress())
                .build();
        EmployeeHelper.updateEmployee(createdEmployee.getId(), updateEmployeeRequest).then().assertThat()
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
        Response response = EmployeeHelper.createTestEmployee(randomEmployee);
        assertNewEmployee(randomEmployee, response);

        // attempt to create employee using same email address
        CreateEmployeeRequest randomEmployee2 = EmployeeHelper.makeRandomCreateEmployeeRequest();
        randomEmployee2.setEmailAddress(randomEmployee.getEmailAddress());
        EmployeeHelper.createTestEmployee(randomEmployee2).then().assertThat()
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
