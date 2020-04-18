package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.FeedbackRequestEntity;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.*;
import net.mattcarpenter.performancereview.service.AuthService;
import net.mattcarpenter.performancereview.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/feedback-requests")
public class FeedbackRequestController {

    private FeedbackRequestService feedbackRequestService;
    private AuthService authService;

    public FeedbackRequestController(FeedbackRequestService feedbackRequestService, AuthService authService) {
        this.feedbackRequestService = feedbackRequestService;
        this.authService = authService;
    }

    @PostMapping
    public FeedbackRequestSummaryModel createFeedbackRequest(@RequestBody CreateFeedbackRequestRequest request) {
        Token token = authService.loadTokenFromSecurityContext();
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestService.createFeedbackRequest(request.getReviewId(),
                request.getReviewerEmployeeId(), request.getDueOn(), request.getFeedbackRequestTemplateId(), token);
        return EntityToModelMapper.mapToFeedbackRequestSummaryModel(feedbackRequestEntity);
    }

    @DeleteMapping(value = "/{feedbackRequestId:.+}")
    public FeedbackRequestDetailsModel deleteFeedbackRequest(@PathVariable UUID feedbackRequestId) {
        Token token = authService.loadTokenFromSecurityContext();
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestService.deactivateFeedbackRequest(feedbackRequestId, token);
        return EntityToModelMapper.mapToFeedbackRequestDetailsModel(feedbackRequestEntity);

    }

    @GetMapping(value = "/{feedbackRequestId:.+}")
    public FeedbackRequestDetailsModel getFeedbackRequestDetails(@PathVariable UUID feedbackRequestId) {
        Token token = authService.loadTokenFromSecurityContext();
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestService.getFeedbackRequest(feedbackRequestId, token);
        return EntityToModelMapper.mapToFeedbackRequestDetailsModel(feedbackRequestEntity);
    }

    @PostMapping(value = "/{feedbackRequestId:.+}/submit")
    public void submitFeedbackRequest(@PathVariable UUID feedbackRequestId) {
        feedbackRequestService.submitFeedbackRequest(feedbackRequestId);
    }

    @PostMapping(value = "/{feedbackRequestId:.+}")
    public FeedbackRequestDetailsModel updateFeedback(@PathVariable UUID feedbackRequestId,
                                                      @RequestBody UpdateFeedbackRequestRequest request) {
        FeedbackRequestEntity feedbackRequestEntity;

        // TODO - auth / role checks
        // TODO - add comments to make pretty
        if (request.getQuestions() != null) {
            feedbackRequestEntity = feedbackRequestService.updateFeedbackRequestQuestions(feedbackRequestId,
                    request.getQuestions());
        } else if (request.getDueOn() != null) {
            feedbackRequestEntity = feedbackRequestService.updateFeedbackRequestDueOn(feedbackRequestId, request.getDueOn());
        } else {
            throw new BadRequestException(ErrorCode.BAD_REQUEST);
        }

        return EntityToModelMapper.mapToFeedbackRequestDetailsModel(feedbackRequestEntity);
    }
}
