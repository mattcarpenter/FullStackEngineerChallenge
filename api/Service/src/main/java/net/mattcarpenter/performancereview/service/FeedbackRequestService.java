package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.*;
import net.mattcarpenter.performancereview.entity.*;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.QuestionModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class FeedbackRequestService {

    FeedbackRequestDao feedbackRequestDao;
    PerformanceReviewDao performanceReviewDao;
    FeedbackRequestTemplateDao feedbackRequestTemplateDao;
    EmployeeDao employeeDao;
    FeedbackResponseDao feedbackResponseDao;

    public FeedbackRequestService(FeedbackRequestDao feedbackRequestDao, PerformanceReviewDao performanceReviewDao,
                                  EmployeeDao employeeDao, FeedbackRequestTemplateDao feedbackRequestTemplateDao,
                                  FeedbackResponseDao feedbackResponseDao) {

        this.feedbackRequestDao = feedbackRequestDao;
        this.performanceReviewDao = performanceReviewDao;
        this.employeeDao = employeeDao;
        this.feedbackRequestTemplateDao = feedbackRequestTemplateDao;
        this.feedbackResponseDao = feedbackResponseDao;
    }

    public FeedbackRequestEntity getFeedbackRequest(UUID feedbackRequestId) {
        return feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
    }

    public FeedbackRequestEntity createFeedbackRequest(UUID reviewId, UUID reviewerEmployeeId, Date dueOn,
                                                       UUID feedbackRequestTemplateId) {

        PerformanceReviewEntity review = performanceReviewDao.findById(reviewId).orElseThrow();
        EmployeeEntity reviewer = employeeDao.findById(reviewerEmployeeId).orElseThrow();
        FeedbackRequestTemplateEntity template = feedbackRequestTemplateDao.findById(feedbackRequestTemplateId).orElseThrow();

        FeedbackRequestEntity feedbackRequest = new FeedbackRequestEntity();
        feedbackRequest.setPerformanceReview(review);
        feedbackRequest.setReviewer(reviewer);
        feedbackRequest.setDueOn(dueOn);
        feedbackRequest.setTemplate(template);
        feedbackRequestDao.save(feedbackRequest);

        return feedbackRequest;
    }

    public FeedbackRequestEntity updateFeedbackRequestQuestions(UUID feedbackRequestId, List<QuestionModel> questions) {
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
        Set<FeedbackResponseEntity> responses = feedbackRequestEntity.getReviewFeedback();
        Set<FeedbackRequestTemplateFieldEntity> templateFields = feedbackRequestEntity.getTemplate().getFields();
        List<FeedbackResponseEntity> responsesToSave = new ArrayList<>();

        if (feedbackRequestEntity.getSubmittedOn() != null) {
            throw new BadRequestException(ErrorCode.FEEDBACK_REQUEST_ALREADY_SUBMITTED);
        }

        questions.forEach(question -> {
            FeedbackResponseEntity response = getFeedbackResponseForTemplateField(question.getFieldId(), responses);
            if (response == null) {

                // feedback record does not yet exist in the database. create a new one
                response = new FeedbackResponseEntity();
                response.setTemplateField(templateFields.stream()
                        .filter(f -> f.getId().equals(question.getFieldId()))
                        .findFirst()
                        .orElseThrow());
                response.setFeedbackRequest(feedbackRequestEntity);
            }

            // update response using the value provided in the request
            response.setResponse(question.getResponse());
            if (!isResponseValid(question, templateFields)) {
                throw new BadRequestException(ErrorCode.FEEDBACK_REQUEST_INVALID_RESPONSE);
            }

            // perform updates later. we'll bail early if there are any validation errors
            responsesToSave.add(response);
        });

        responsesToSave.forEach(r -> feedbackResponseDao.save(r));

        return feedbackRequestEntity;
    }

    public FeedbackRequestEntity updateFeedbackRequestDueOn(UUID feedbackRequestId, Date dueOn) {
        FeedbackRequestEntity entity = feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
        entity.setDueOn(dueOn);
        feedbackRequestDao.save(entity);
        return entity;
    }

    public void submitFeedbackRequest(UUID feedbackRequestId) {
        FeedbackRequestEntity feedbackRequestEntity = feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
        Set<FeedbackRequestTemplateFieldEntity> expectedFields = feedbackRequestEntity.getTemplate().getFields();
        Set<FeedbackResponseEntity> responses = feedbackRequestEntity.getReviewFeedback();

        if (expectedFields.size() != responses.size()) {
            // future improvement - implement enhanced server-side validation that indicates which fields are missing
            throw new BadRequestException(ErrorCode.FEEDBACK_REQUEST_INCOMPLETE);
        }

        if (feedbackRequestEntity.getSubmittedOn() != null) {
            throw new BadRequestException(ErrorCode.FEEDBACK_REQUEST_ALREADY_SUBMITTED);
        }

        feedbackRequestEntity.setSubmittedOn(new Date());
        feedbackRequestDao.save(feedbackRequestEntity);
    }

    private FeedbackResponseEntity getFeedbackResponseForTemplateField(UUID fieldId, Set<FeedbackResponseEntity> responses) {
       return responses.stream().filter(r -> r.getTemplateField().getId().equals(fieldId)).findFirst().orElse(null);
    }

    private boolean isResponseValid(QuestionModel question,
                                    Set<FeedbackRequestTemplateFieldEntity> fields) {
        FeedbackRequestTemplateFieldEntity field = fields.stream()
                .filter(f -> f.getId().equals(question.getFieldId()))
                .findFirst()
                .orElseThrow();
        return !"MULTIPLE_CHOICE".equals(field.getType()) || EntityToModelMapper.mapToChoicesList(field).contains(question.getResponse());
    }
}
