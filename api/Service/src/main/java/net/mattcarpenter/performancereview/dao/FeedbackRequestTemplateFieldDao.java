package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.FeedbackRequestTemplateFieldEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface FeedbackRequestTemplateFieldDao extends CrudRepository<FeedbackRequestTemplateFieldEntity, UUID> {}
