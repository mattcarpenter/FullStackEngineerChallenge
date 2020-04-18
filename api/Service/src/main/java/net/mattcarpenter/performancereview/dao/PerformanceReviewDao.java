package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.PerformanceReviewEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PerformanceReviewDao extends CrudRepository<PerformanceReviewEntity, UUID> {}
