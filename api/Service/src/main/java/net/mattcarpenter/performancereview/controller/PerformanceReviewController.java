package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.PerformanceReviewEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.CreatePerformanceReviewRequest;
import net.mattcarpenter.performancereview.model.PerformanceReviewModel;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.service.AuthService;
import net.mattcarpenter.performancereview.service.PerformanceReviewService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/performance-reviews")
public class PerformanceReviewController {

    PerformanceReviewService performanceReviewService;
    AuthService authService;

    public PerformanceReviewController(PerformanceReviewService performanceReviewService, AuthService authService) {
        this.performanceReviewService = performanceReviewService;
        this.authService = authService;
    }

    @PostMapping
    public PerformanceReviewModel createPerformanceReview(@RequestBody @Valid CreatePerformanceReviewRequest request) {
        Token token = authService.loadTokenFromSecurityContext();
        PerformanceReviewEntity entity = performanceReviewService.createReview(request.getReviewee(), request.getMemo(), token);
        return EntityToModelMapper.mapToReviewModel(entity);
    }

    @DeleteMapping(value = "/{reviewId:.+}")
    public void deleteReview(@PathVariable UUID reviewId) {
        Token token = authService.loadTokenFromSecurityContext();
        performanceReviewService.deleteReview(reviewId, token);
    }

    @GetMapping(value = "/{reviewId:.+}")
    public PerformanceReviewModel getPerformanceReview(@PathVariable UUID reviewId) {
        Token token = authService.loadTokenFromSecurityContext();
        PerformanceReviewEntity entity = performanceReviewService.getReview(reviewId, token);
        return EntityToModelMapper.mapToReviewModel(entity);
    }
}
