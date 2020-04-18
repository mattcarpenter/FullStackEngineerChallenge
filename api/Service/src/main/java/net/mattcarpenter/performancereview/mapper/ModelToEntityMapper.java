package net.mattcarpenter.performancereview.mapper;

import net.mattcarpenter.performancereview.entity.CredentialEntity;
import net.mattcarpenter.performancereview.model.CredentialModel;

public class ModelToEntityMapper {

    public static CredentialEntity mapToCredentialEntity(CredentialModel model) {
        CredentialEntity entity = new CredentialEntity();
        entity.setPassword(model.getPassword());
        entity.setPasswordSalt(model.getPasswordSalt());
        entity.setPasswordHashAlgorithm(model.getPasswordHashAlgorithm());
        entity.setIterations(model.getIterations());
        return entity;
    }
}
