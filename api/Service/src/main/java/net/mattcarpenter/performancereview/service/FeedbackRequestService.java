package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.*;
import net.mattcarpenter.performancereview.entity.*;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.exception.NotAuthorizedException;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.QuestionModel;
import net.mattcarpenter.performancereview.model.TemplateFieldModel;
import net.mattcarpenter.performancereview.model.TemplateModel;
import net.mattcarpenter.performancereview.model.Token;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class FeedbackRequestService {

    FeedbackRequestDao feedbackRequestDao;
    PerformanceReviewDao performanceReviewDao;
    FeedbackRequestTemplateDao feedbackRequestTemplateDao;
    FeedbackRequestTemplateFieldDao feedbackRequestTemplateFieldDao;
    EmployeeDao employeeDao;
    FeedbackResponseDao feedbackResponseDao;

    public FeedbackRequestService(FeedbackRequestDao feedbackRequestDao, PerformanceReviewDao performanceReviewDao,
                                  EmployeeDao employeeDao, FeedbackRequestTemplateDao feedbackRequestTemplateDao,
                                  FeedbackResponseDao feedbackResponseDao,
                                  FeedbackRequestTemplateFieldDao feedbackRequestTemplateFieldDao) {

        this.feedbackRequestDao = feedbackRequestDao;
        this.performanceReviewDao = performanceReviewDao;
        this.employeeDao = employeeDao;
        this.feedbackRequestTemplateDao = feedbackRequestTemplateDao;
        this.feedbackResponseDao = feedbackResponseDao;
        this.feedbackRequestTemplateFieldDao = feedbackRequestTemplateFieldDao;
    }

    public FeedbackRequestEntity getFeedbackRequest(UUID feedbackRequestId, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        return feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
    }

    public FeedbackRequestEntity deactivateFeedbackRequest(UUID feedbackRequestId, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        FeedbackRequestEntity entity = feedbackRequestDao.findById(feedbackRequestId).orElseThrow();
        entity.setDeleted(true);
        feedbackRequestDao.save(entity);
        return entity;
    }

    public FeedbackRequestEntity createFeedbackRequest(UUID reviewId, UUID reviewerEmployeeId, Date dueOn,
                                                       UUID feedbackRequestTemplateId, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

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

    /**
     * Creates a feedback request template field. This is only used by functional tests.
     * @param field
     * @return field id
     */
    public UUID createFeedbackRequestTemplateField(TemplateFieldModel field) {
        FeedbackRequestTemplateEntity templateEntity = feedbackRequestTemplateDao.findById(field.getTemplateId()).orElseThrow();
        FeedbackRequestTemplateFieldEntity fieldEntity = new FeedbackRequestTemplateFieldEntity();
        fieldEntity.setFeedbackRequestTemplate(templateEntity);
        fieldEntity.setChoice1(field.getChoice1());
        fieldEntity.setChoice2(field.getChoice2());
        fieldEntity.setChoice3(field.getChoice3());
        fieldEntity.setChoice4(field.getChoice4());
        fieldEntity.setChoice5(field.getChoice5());
        fieldEntity.setChoiceCount(field.getChoiceCount());
        fieldEntity.setPrompt(field.getPrompt());
        fieldEntity.setType(field.getType());
        feedbackRequestTemplateFieldDao.save(fieldEntity);
        return fieldEntity.getId();
    }

    /**
     * Creates a feedback request template. This is only used by functional tests.
     * @param name Name of template e.g. Self Review, Peer Review, etc...
     * @return field id
     */
    public UUID createFeedbackRequestTemplate(String name) {
        FeedbackRequestTemplateEntity templateEntity = new FeedbackRequestTemplateEntity();
        templateEntity.setName(name);
        feedbackRequestTemplateDao.save(templateEntity);
        return templateEntity.getId();
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
