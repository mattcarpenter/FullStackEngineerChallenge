package net.mattcarpenter.performancereview.controller;

import net.mattcarpenter.performancereview.entity.PerformanceReviewEntity;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.CreatePerformanceReviewRequest;
import net.mattcarpenter.performancereview.model.PerformanceReviewModel;
import net.mattcarpenter.performancereview.service.PerformanceReviewService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/performance-reviews")
public class PerformanceReviewController {

    PerformanceReviewService performanceReviewService;

    public PerformanceReviewController(PerformanceReviewService performanceReviewService) {
        this.performanceReviewService = performanceReviewService;
    }

    @PostMapping
    public PerformanceReviewModel createPerformanceReview(@RequestBody @Valid CreatePerformanceReviewRequest request) {
        PerformanceReviewEntity entity = performanceReviewService.createReview(request.getReviewee());
        return EntityToModelMapper.mapToReviewModel(entity);
    }

    @DeleteMapping(value = "/{reviewId:.+}")
    public void deleteReview(@PathVariable UUID reviewId) {
        performanceReviewService.deleteReview(reviewId);
    }

    @GetMapping(value = "/{reviewId:.+}")
    public PerformanceReviewModel getPerformanceReview(@PathVariable UUID reviewId) {
        PerformanceReviewEntity entity = performanceReviewService.getReview(reviewId);
        return EntityToModelMapper.mapToReviewModel(entity);
    }
}
