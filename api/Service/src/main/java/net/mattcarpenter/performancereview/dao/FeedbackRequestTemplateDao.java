package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.FeedbackRequestTemplateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeedbackRequestTemplateDao extends CrudRepository<FeedbackRequestTemplateEntity, UUID> {}
