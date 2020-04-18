package net.mattcarpenter.performancereview.functionaltests.helpers;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.mattcarpenter.performancereview.constants.Constants;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.utils.TestUtils;
import net.mattcarpenter.performancereview.model.CreateEmployeeRequest;
import net.mattcarpenter.performancereview.model.LoginRequest;
import net.mattcarpenter.performancereview.model.UpdateEmployeeRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class EmployeeHelper {

    public static final String TEST_PASSWORD = "Test1234$$";
    public static final String EMAIL_TEMPLATE = "%s%s%s@mattcarpenter.net";

    public static Response createTestEmployee(CreateEmployeeRequest request, String token) {
        RequestSpecification createEmployeeRequest = RestAssured.given();
        createEmployeeRequest.body(request);

        if (StringUtils.isNotEmpty(token)) {
            createEmployeeRequest.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }

        createEmployeeRequest.contentType(ContentType.JSON);
        return createEmployeeRequest.post(TestUtils.makePath(TestConstants.V1_EMPLOYEE_CREATE));
    }

    public static Response loginTestEmployee(String emailAddress) {
        LoginRequest request = new LoginRequest(emailAddress, TEST_PASSWORD);
        RequestSpecification loginEmployeeRequest = RestAssured.given();
        loginEmployeeRequest.body(request);
        loginEmployeeRequest.contentType(ContentType.JSON);
        return loginEmployeeRequest.post(TestUtils.makePath(TestConstants.V1_LOGIN));
    }

    public static Response getEmployee(UUID employeeId, String token) {
        RequestSpecification request = RestAssured.given();
        request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        return request.get(TestUtils.makePath(TestConstants.V1_EMPLOYEE_GET, employeeId.toString()));
    }

    public static Response updateEmployee(UUID employeeId, UpdateEmployeeRequest request, String token) {
        RequestSpecification updateRequest = RestAssured.given();
        updateRequest.body(request);
        updateRequest.cookie(Constants.TOKEN_COOKIE_NAME, token);
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

    public static String getAdminToken() {
        CreateEmployeeRequest createRequestBody = makeRandomCreateEmployeeRequest();
        createRequestBody.setAdmin(true);
        RequestSpecification createRequest = RestAssured.given();
        createRequest.body(createRequestBody);
        createRequest.contentType(ContentType.JSON);
        createRequest.post(TestUtils.makePath(TestConstants.V1_ADMIN_EMPLOYEE_CREATE));

        LoginRequest loginRequestBody = new LoginRequest(createRequestBody.getEmailAddress(), createRequestBody.getPassword());
        RequestSpecification loginRequest = RestAssured.given();
        loginRequest.body(loginRequestBody);
        loginRequest.contentType(ContentType.JSON);
        return loginRequest.post(TestUtils.makePath(TestConstants.V1_LOGIN))
                .getCookie(Constants.TOKEN_COOKIE_NAME);

    }
}
