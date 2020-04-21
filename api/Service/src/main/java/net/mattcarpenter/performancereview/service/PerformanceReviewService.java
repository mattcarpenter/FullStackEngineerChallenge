package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.EmployeeDao;
import net.mattcarpenter.performancereview.dao.PerformanceReviewDao;
import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.entity.PerformanceReviewEntity;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.NotAuthorizedException;
import net.mattcarpenter.performancereview.model.Token;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class PerformanceReviewService {

    PerformanceReviewDao performanceReviewDao;
    EmployeeDao employeeDao;

    public PerformanceReviewService(PerformanceReviewDao performanceReviewDao, EmployeeDao employeeDao) {
        this.performanceReviewDao = performanceReviewDao;
        this.employeeDao = employeeDao;
    }

    public PerformanceReviewEntity createReview(UUID revieweeId, String memo, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        EmployeeEntity employeeEntity = employeeDao.findById(revieweeId).orElseThrow();
        PerformanceReviewEntity performanceReviewEntity = new PerformanceReviewEntity();
        performanceReviewEntity.setRevieweeEmployee(employeeEntity);
        performanceReviewEntity.setMemo(memo);
        performanceReviewDao.save(performanceReviewEntity);
        return performanceReviewEntity;
    }

    public PerformanceReviewEntity deleteReview(UUID reviewId, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        PerformanceReviewEntity performanceReviewEntity = performanceReviewDao.findById(reviewId).orElseThrow();
        performanceReviewEntity.setDeleted(true);
        performanceReviewDao.save(performanceReviewEntity);
        return performanceReviewEntity;
    }

    public PerformanceReviewEntity getReview(UUID reviewId, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_PASSWORD);
        }

        return performanceReviewDao.findById(reviewId).orElseThrow();
    }
}
