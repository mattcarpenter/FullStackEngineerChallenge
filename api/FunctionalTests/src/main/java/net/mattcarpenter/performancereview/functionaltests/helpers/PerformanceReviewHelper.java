package net.mattcarpenter.performancereview.functionaltests.helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.internal.TestSpecificationImpl;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.mattcarpenter.performancereview.constants.Constants;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.utils.TestUtils;
import net.mattcarpenter.performancereview.model.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PerformanceReviewHelper {
    public static Response createPerformanceReview(UUID revieweeEmployeeId, String token) {
        CreatePerformanceReviewRequest requestBody = new CreatePerformanceReviewRequest(revieweeEmployeeId);
        RequestSpecification request = RestAssured.given();
        request.body(requestBody);
        request.contentType(ContentType.JSON);
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.post(TestUtils.makePath(TestConstants.V1_CREATE_PERFORMANCE_REVIEW));
    }

    public static Response createFeedbackRequest(UUID performanceReviewId, UUID reviewerEmployeeId,
                                                 UUID feedbackRequestTemplateFieldId, Date dueOn, String token) {
        CreateFeedbackRequestRequest requestBody = CreateFeedbackRequestRequest.builder()
                .feedbackRequestTemplateId(feedbackRequestTemplateFieldId)
                .dueOn(dueOn)
                .reviewerEmployeeId(reviewerEmployeeId)
                .reviewId(performanceReviewId)
                .build();

        RequestSpecification request = RestAssured.given();
        request.body(requestBody);
        request.contentType(ContentType.JSON);
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.post(TestUtils.makePath(TestConstants.V1_CREATE_FEEDBACK_REQUEST));
    }

    public static Response deleteFeedbackRequest(UUID feedbackRequestId, String token) {
        RequestSpecification request = RestAssured.given();
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.delete(TestUtils.makePath(TestConstants.V1_DELETE_FEEDBACK_REQUEST, feedbackRequestId.toString()));
    }

    public static Response getPerformanceReview(UUID performanceReviewId, String token) {
        RequestSpecification request = RestAssured.given();
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.get(TestUtils.makePath(TestConstants.V1_GET_PERFORMANCE_REVIEW, performanceReviewId.toString()));
    }

    public static Response getFeedbackRequestDetails(UUID feedbackRequestId, String token) {
        RequestSpecification request = RestAssured.given();
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.get(TestUtils.makePath(TestConstants.V1_GET_FEEDBACK_REQUEST, feedbackRequestId.toString()));
    }

    public static UUID createFeedbackTemplate(String name) {
        TemplateModel template = new TemplateModel(name);
        RequestSpecification request = RestAssured.given();
        request.body(template);
        request.contentType(ContentType.JSON);
        return UUID.fromString(request.post(TestUtils.makePath(TestConstants.V1_CREATE_TEMPLATE)).asString());
    }

    public static void createFeedbackTemplateField(TemplateFieldModel field) {
        RequestSpecification request = RestAssured.given();
        request.body(field);
        request.contentType(ContentType.JSON);
        request.post(TestUtils.makePath(TestConstants.V1_CREATE_TEMPLATE_FIELD));
    }

    public static Response submitFeedbackRequest(UUID feedbackRequestId, String token) {
        RequestSpecification request = RestAssured.given();
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.post(TestUtils.makePath(TestConstants.V1_SUBMIT_FEEDBACK_REQUEST, feedbackRequestId.toString()));
    }

    public static Response updateFeedbackResponseQuestions(UUID feedbackRequestId, List<QuestionModel> questions, String token) {
        UpdateFeedbackRequestRequest requestBody = UpdateFeedbackRequestRequest.builder()
                .questions(questions)
                .build();
        RequestSpecification request = RestAssured.given();
        request.body(requestBody);
        request.contentType(ContentType.JSON);
        if (StringUtils.isNotEmpty(token)) {
            request.cookie(Constants.TOKEN_COOKIE_NAME, token);
        }
        return request.post(TestUtils.makePath(TestConstants.V1_UPDATE_FEEDBACK_REQUEST, feedbackRequestId.toString()));
    }
}
