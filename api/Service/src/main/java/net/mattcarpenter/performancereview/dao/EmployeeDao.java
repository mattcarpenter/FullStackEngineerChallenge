package net.mattcarpenter.performancereview.dao;

import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmployeeDao extends CrudRepository<EmployeeEntity, UUID> {
    boolean existsByEmailAddress(String emailAddress);
    EmployeeEntity findByEmailAddress(String emailAddress);
}
