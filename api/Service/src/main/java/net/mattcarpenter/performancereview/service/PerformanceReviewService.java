package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.EmployeeDao;
import net.mattcarpenter.performancereview.dao.PerformanceReviewDao;
import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.entity.PerformanceReviewEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
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

    public PerformanceReviewEntity createReview(UUID revieweeId) {
        EmployeeEntity employeeEntity = employeeDao.findById(revieweeId).orElseThrow();
        PerformanceReviewEntity performanceReviewEntity = new PerformanceReviewEntity();
        performanceReviewEntity.setRevieweeEmployee(employeeEntity);
        // todo - set reviewer
        performanceReviewDao.save(performanceReviewEntity);
        return performanceReviewEntity;
    }

    public PerformanceReviewEntity deleteReview(UUID reviewId) {
        PerformanceReviewEntity performanceReviewEntity = performanceReviewDao.findById(reviewId).orElseThrow();
        performanceReviewEntity.setDeleted(true);
        performanceReviewDao.save(performanceReviewEntity);
        return performanceReviewEntity;
    }

    public PerformanceReviewEntity getReview(UUID reviewId) {
        return performanceReviewDao.findById(reviewId).orElseThrow();
    }
}
