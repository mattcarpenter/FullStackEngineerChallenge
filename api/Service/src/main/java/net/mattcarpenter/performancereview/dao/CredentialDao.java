package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.CredentialEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CredentialDao extends CrudRepository<CredentialEntity, UUID> {}
