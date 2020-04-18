package net.mattcarpenter.performancereview.functionaltests.constants;

public class TestConstants {

    /* endpoint templates */
    public static final String V1_ADMIN_EMPLOYEE_CREATE = "/api/v1/admin/employees";
    public static final String V1_EMPLOYEE_CREATE = "/api/v1/employees";
    public static final String V1_EMPLOYEE_GET = "/api/v1/employees/%s";
    public static final String V1_EMPLOYEE_UPDATE = "/api/v1/employees/%s";
    public static final String V1_LOGIN = "/api/v1/auth/login";

    /* error codes */
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String FEEDBACK_REQUEST_ALREADY_SUBMITTED = "FEEDBACK_REQUEST_ALREADY_SUBMITTED";
    public static final String FEEDBACK_REQUEST_INCOMPLETE = "FEEDBACK_REQUEST_INCOMPLETE";
    public static final String FEEDBACK_REQUEST_INVALID_RESPONSE = "FEEDBACK_REQUEST_INVALID_RESPONSE";
    public static final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
    public static final String INTERNAL_SERVICE_ERROR = "INTERNAL_SERVICE_ERROR";
}
