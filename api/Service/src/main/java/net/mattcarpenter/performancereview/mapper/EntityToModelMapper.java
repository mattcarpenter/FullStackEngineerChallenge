package net.mattcarpenter.performancereview.mapper;

import net.mattcarpenter.performancereview.entity.*;
import net.mattcarpenter.performancereview.model.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
public class EntityToModelMapper {

    public static EmployeeModel mapToEmployeeModel(EmployeeEntity entity) {
        List<FeedbackRequestSummaryModel> feedbackRequests = entity.getFeedbackRequests().stream()
                .map(EntityToModelMapper::mapToFeedbackRequestSummaryModel)
                .collect(Collectors.toList());

        return EmployeeModel.builder()
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailAddress(entity.getEmailAddress())
                .id(entity.getId())
                .feedbackRequests(feedbackRequests)
                .build();
    }

    public static PerformanceReviewModel mapToReviewModel(PerformanceReviewEntity entity) {

        List<FeedbackRequestSummaryModel> feedbackRequests = entity.getReviewFeedback().stream()
                .map(EntityToModelMapper::mapToFeedbackRequestSummaryModel)
                .collect(Collectors.toList());

        return PerformanceReviewModel.builder()
                .id(entity.getId())
                .revieweeEmployeeId(entity.getRevieweeEmployee().getId())
                .createdAt(entity.getCreatedAt())
                .feedbackRequests(feedbackRequests)
                .build();
    }

    public static FeedbackRequestSummaryModel mapToFeedbackRequestSummaryModel(FeedbackRequestEntity feedbackRequestEntity) {
        EmployeeEntity reviewer = feedbackRequestEntity.getReviewer();
        EmployeeEntity reviewee = feedbackRequestEntity.getPerformanceReview().getRevieweeEmployee();

        return FeedbackRequestSummaryModel.builder()
                .dueOn(feedbackRequestEntity.getDueOn())
                .submittedOn(feedbackRequestEntity.getSubmittedOn())
                .id(feedbackRequestEntity.getId())
                .reviewer(EntityToModelMapper.mapToEmployeePublicDataModel(reviewer))
                .reviewee(EntityToModelMapper.mapToEmployeePublicDataModel(reviewee))
                .templateName(feedbackRequestEntity.getTemplate().getName())
                .build();
    }

    public static FeedbackRequestDetailsModel mapToFeedbackRequestDetailsModel(FeedbackRequestEntity feedbackRequestEntity) {
        EmployeeEntity reviewerEntity = feedbackRequestEntity.getReviewer();
        EmployeeEntity revieweeEntity = feedbackRequestEntity.getPerformanceReview().getRevieweeEmployee();

        // Build a list of questions by merging template fields and any given responses for the current feedback request
        Map<UUID, String> responses = feedbackRequestEntity.getReviewFeedback().stream()
                .collect(Collectors.toMap(e -> e.getTemplateField().getId(), FeedbackResponseEntity::getResponse));

        List<QuestionModel> questions = feedbackRequestEntity.getTemplate().getFields().stream()
                .map(f -> QuestionModel.builder()
                        .fieldId(f.getId())
                        .type(f.getType())
                        .prompt(f.getPrompt())
                        .choices(mapToChoicesList(f))
                        .response(responses.get(f.getId()))
                        .build())
                .collect(Collectors.toList());

        return FeedbackRequestDetailsModel.builder()
                .id(feedbackRequestEntity.getId())
                .reviewer(mapToEmployeePublicDataModel(reviewerEntity))
                .reviewee(mapToEmployeePublicDataModel(revieweeEntity))
                .questions(questions)
                .dueOn(feedbackRequestEntity.getDueOn())
                .submittedOn(feedbackRequestEntity.getSubmittedOn())
                .build();
    }

    public static EmployeePublicDataModel mapToEmployeePublicDataModel(EmployeeEntity entity) {
        return new EmployeePublicDataModel(entity.getFirstName(), entity.getLastName());
    }

    public static List<String> mapToChoicesList(FeedbackRequestTemplateFieldEntity fieldEntity) {
        List<String> choices = Arrays.asList(fieldEntity.getChoice1(), fieldEntity.getChoice2(), fieldEntity.getChoice3(),
                fieldEntity.getChoice4(), fieldEntity.getChoice5());
        return choices.subList(0, fieldEntity.getChoiceCount());
    }

    public static CredentialModel mapToCredentialModel(CredentialEntity entity) {
        return CredentialModel.builder()
                .password(entity.getPassword())
                .iterations(entity.getIterations())
                .passwordHashAlgorithm(entity.getPasswordHashAlgorithm())
                .passwordSalt(entity.getPasswordSalt())
                .build();
    }
}
