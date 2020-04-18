package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.FeedbackResponseEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeedbackResponseDao extends CrudRepository<FeedbackResponseEntity, UUID> {}
