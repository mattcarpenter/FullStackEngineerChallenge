package net.mattcarpenter.performancereview.functionaltests.helpers;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.utils.TestUtils;
import net.mattcarpenter.performancereview.model.CreateEmployeeRequest;
import net.mattcarpenter.performancereview.model.UpdateEmployeeRequest;

import java.util.UUID;

public class EmployeeHelper {

    public static final String TEST_PASSWORD = "Test1234$$";
    public static final String EMAIL_TEMPLATE = "%s%s%s@mattcarpenter.net";

    public static Response createTestEmployee(CreateEmployeeRequest request) {
        RequestSpecification createEmployeeRequest = RestAssured.given();
        createEmployeeRequest.body(request);
        createEmployeeRequest.contentType(ContentType.JSON);
        return createEmployeeRequest.post(TestUtils.makePath(TestConstants.V1_EMPLOYEE_CREATE));
    }

    public static Response getEmployee(UUID employeeId) {
        return RestAssured.given().get(TestUtils.makePath(TestConstants.V1_EMPLOYEE_GET, employeeId.toString()));
    }

    public static Response updateEmployee(UUID employeeId, UpdateEmployeeRequest request) {
        RequestSpecification updateRequest = RestAssured.given();
        updateRequest.body(request);
        updateRequest.contentType(ContentType.JSON);
        return updateRequest.post(TestUtils.makePath(TestConstants.V1_EMPLOYEE_UPDATE, employeeId.toString()));
    }

    public static CreateEmployeeRequest makeRandomCreateEmployeeRequest() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format(EMAIL_TEMPLATE, firstName, TestUtils.randomAlphaNumeric(4),
                lastName);

        return CreateEmployeeRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .emailAddress(email)
                .isAdmin(false)
                .password(TEST_PASSWORD)
                .build();
    }
}
