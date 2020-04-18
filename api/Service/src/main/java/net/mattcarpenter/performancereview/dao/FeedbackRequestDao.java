package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.FeedbackRequestEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeedbackRequestDao extends CrudRepository<FeedbackRequestEntity, UUID> {}
