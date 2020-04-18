package net.mattcarpenter.performancereview.error;


public enum ErrorCode {
    AUTHORIZATION_INVALID_EMAIL("AUTHORIZATION_CREDENTIALS", "Unable to authorize the given credentials"),
    AUTHORIZATION_INVALID_PASSWORD("AUTHORIZATION_CREDENTIALS", "Unable to authorize the given credentials"),
    AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN("AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN", "The token provided is either invalid or expired"),
    BAD_REQUEST("BAD_REQUEST", "There was an issue with the request received"),
    FEEDBACK_REQUEST_ALREADY_SUBMITTED("FEEDBACK_REQUEST_ALREADY_SUBMITTED", "This user already exists"),
    FEEDBACK_REQUEST_INCOMPLETE("FEEDBACK_REQUEST_INCOMPLETE", "Feedback request incomplete"),
    FEEDBACK_REQUEST_INVALID_RESPONSE("FEEDBACK_REQUEST_INVALID_RESPONSE", "Invalid response provided"),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email address already exists in the system"),
    INTERNAL_SERVICE_ERROR("INTERNAL_SERVICE_ERROR", "An internal service error occurred");

    private final String code;
    private final String description;

    private ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
