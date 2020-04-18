package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.FeedbackRequestEntity;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.CreateFeedbackRequestRequest;
import net.mattcarpenter.performancereview.model.FeedbackRequestDetailsModel;
import net.mattcarpenter.performancereview.model.FeedbackRequestSummaryModel;
import net.mattcarpenter.performancereview.model.UpdateFeedbackRequestRequest;
import net.mattcarpenter.performancereview.service.FeedbackRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/feedback-requests")
public class FeedbackRequestController {

    private FeedbackRequestService feedbackRequestService;

    public FeedbackRequestController(FeedbackRequestService feedbackRequestService) {
        this.feedbackRequestService = feedbackRequestService;
    }

    @PostMapping
    public FeedbackRequestSummaryModel createFeedbackRequest(@RequestBody CreateFeedbackRequestRequest request) {
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestService.createFeedbackRequest(request.getReviewId(),
                request.getReviewerEmployeeId(),
                request.getDueOn(), request.getFeedbackRequestTemplateId());
        return EntityToModelMapper.mapToFeedbackRequestSummaryModel(feedbackRequestEntity);
    }

    @GetMapping(value = "/{feedbackRequestId:.+}")
    public FeedbackRequestDetailsModel getFeedbackRequestDetails(@PathVariable UUID feedbackRequestId) {
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestService.getFeedbackRequest(feedbackRequestId);
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
