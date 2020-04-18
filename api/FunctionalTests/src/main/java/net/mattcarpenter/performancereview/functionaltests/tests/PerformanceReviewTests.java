package net.mattcarpenter.performancereview.functionaltests.tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import net.mattcarpenter.performancereview.constants.Constants;
import net.mattcarpenter.performancereview.functionaltests.constants.TestConstants;
import net.mattcarpenter.performancereview.functionaltests.helpers.EmployeeHelper;
import net.mattcarpenter.performancereview.functionaltests.helpers.PerformanceReviewHelper;
import net.mattcarpenter.performancereview.functionaltests.utils.TestUtils;
import net.mattcarpenter.performancereview.model.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class PerformanceReviewTests {

    private String adminToken;
    private UUID templateId;

    private static final String TEMPLATE_NAME = "Peer Review";

    @BeforeClass
    public void before() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        adminToken = EmployeeHelper.getAdminToken();

        // Create a template and template field for use with test feedback requests
        templateId = PerformanceReviewHelper.createFeedbackTemplate(TEMPLATE_NAME);
        TemplateFieldModel field = TemplateFieldModel.builder()
                .type("TEXT")
                .prompt("Please evaluate your peer.")
                .templateId(templateId)
                .build();
        PerformanceReviewHelper.createFeedbackTemplateField(field);
    }

    @Test
    public void createPerformanceReviewAndAddReviewer_scenario_happyPath() {

        // create a reviewee employee and a reviewer employee
        EmployeeModel reviewee = EmployeeHelper.createTestEmployee(EmployeeHelper.makeRandomCreateEmployeeRequest(), adminToken)
                .as(EmployeeModel.class);
        EmployeeModel reviewer = EmployeeHelper.createTestEmployee(EmployeeHelper.makeRandomCreateEmployeeRequest(), adminToken)
                .as(EmployeeModel.class);

        // create a performance review for the reviewee
        Response createPerformanceReviewResponse = PerformanceReviewHelper.createPerformanceReview(reviewee.getId(), adminToken);
        createPerformanceReviewResponse.then()
                .body("revieweeEmployeeId", equalTo(reviewee.getId().toString()))
                .body("feedbackRequests", empty());
        PerformanceReviewModel performanceReview = createPerformanceReviewResponse.as(PerformanceReviewModel.class);

        // create a feedback request for the new performance review and assign it to the reviewer
        Response feedbackRequestResponse = PerformanceReviewHelper.createFeedbackRequest(performanceReview.getId(),
                reviewer.getId(), templateId, TestUtils.getTomorrow(), adminToken);
        feedbackRequestResponse.then()
                .body("templateName", equalTo(TEMPLATE_NAME));
        FeedbackRequestSummaryModel feedbackRequestDetails = feedbackRequestResponse.as(FeedbackRequestSummaryModel.class);

        // verify performance review now contains one reviewer
        PerformanceReviewHelper.getPerformanceReview(performanceReview.getId(), adminToken).then()
                .body("feedbackRequests", hasItems(anything())); // enhance these assertions later

        // delete feedback request
        PerformanceReviewHelper.deleteFeedbackRequest(feedbackRequestDetails.getId(), adminToken);

        // get the performance review and assert no feedback requests are assigned
        PerformanceReviewHelper.getPerformanceReview(performanceReview.getId(), adminToken).then()
                .body("feedbackRequests", empty());
    }

    @Test
    public void createPerformanceReview_nonAdmin_unauthorized() {
        EmployeeModel reviewee = EmployeeHelper.createTestEmployee(EmployeeHelper.makeRandomCreateEmployeeRequest(), adminToken)
                .as(EmployeeModel.class);
        String employeeToken = EmployeeHelper.loginTestEmployee(reviewee.getEmailAddress()).cookie(Constants.TOKEN_COOKIE_NAME);
        PerformanceReviewHelper.createPerformanceReview(reviewee.getId(), employeeToken).then()
                .body("code", equalTo(TestConstants.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN));
    }
}
