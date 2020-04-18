package net.mattcarpenter.performancereview.service;

import com.google.common.collect.Lists;
import net.mattcarpenter.performancereview.dao.CredentialDao;
import net.mattcarpenter.performancereview.dao.EmployeeDao;
import net.mattcarpenter.performancereview.entity.CredentialEntity;
import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.exception.NotAuthorizedException;
import net.mattcarpenter.performancereview.mapper.ModelToEntityMapper;
import net.mattcarpenter.performancereview.model.CredentialModel;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.utils.Crypto;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {

    CredentialDao credentialDao;
    EmployeeDao employeeDao;

    public EmployeeService(CredentialDao credentialDao, EmployeeDao employeeDao) {
        this.credentialDao = credentialDao;
        this.employeeDao = employeeDao;
    }

    public EmployeeEntity getEmployee(Token token) {
        return getEmployee(token.getEmployeeId(), token);
    }

    public EmployeeEntity getEmployee(UUID id, Token token) {
        if (!id.equals(token.getEmployeeId()) && !token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        return employeeDao.findById(id).orElseThrow();
    }

    public List<EmployeeEntity> getEmployees() {
        return Lists.newArrayList(employeeDao.findAll());
    }

    public EmployeeEntity deactivateEmployee(UUID id) {
        EmployeeEntity employeeEntity = employeeDao.findById(id).orElseThrow();
        employeeEntity.setActive(false);
        employeeDao.save(employeeEntity);
        return employeeEntity;
    }

    public EmployeeEntity createEmployee(String firstName, String lastName, String emailAddress, String password,
                                         boolean isAdmin, Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        CredentialModel credentialModel = null;

        try {
            credentialModel = Crypto.generatePasswordHashAndSalt(password);
        } catch (Exception ex) {
            throw new BadRequestException(ErrorCode.INTERNAL_SERVICE_ERROR);
        }

        // ensure user does not already exist
        if (employeeDao.existsByEmailAddress(emailAddress)) {
            throw new BadRequestException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // Create and store employee
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setFirstName(firstName);
        employeeEntity.setLastName(lastName);
        employeeEntity.setEmailAddress(emailAddress);
        employeeEntity.setActive(true);
        employeeEntity.setAdmin(isAdmin);
        employeeEntity.setCreatedBy(token.getEmployeeId());
        employeeDao.save(employeeEntity);

        // Create and store credential
        CredentialEntity credentialEntity = ModelToEntityMapper.mapToCredentialEntity(credentialModel);
        credentialEntity.setEmployee(employeeEntity);
        credentialDao.save(credentialEntity);

        return employeeEntity;
    }

    public EmployeeEntity updateEmployee(UUID employeeId, String firstName, String lastName, String emailAddress,
                                         Token token) {
        if (!token.isAdmin()) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        EmployeeEntity employeeEntity = employeeDao.findById(employeeId).orElseThrow();

        if (!StringUtils.isEmpty(firstName)) {
            employeeEntity.setFirstName(firstName);
        }

        if (!StringUtils.isEmpty(lastName)) {
            employeeEntity.setLastName(lastName);
        }

        if (!StringUtils.isEmpty(emailAddress)) {
            if (!emailAddress.equals(employeeEntity.getEmailAddress()) && employeeDao.existsByEmailAddress(emailAddress)) {
                throw new BadRequestException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }
            employeeEntity.setEmailAddress(emailAddress);
        }

        employeeDao.save(employeeEntity);
        return employeeEntity;
    }
}
