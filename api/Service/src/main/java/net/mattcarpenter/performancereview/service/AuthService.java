package net.mattcarpenter.performancereview.service;

import net.mattcarpenter.performancereview.dao.EmployeeDao;
import net.mattcarpenter.performancereview.entity.EmployeeEntity;
import net.mattcarpenter.performancereview.error.ErrorCode;
import net.mattcarpenter.performancereview.exception.BadRequestException;
import net.mattcarpenter.performancereview.exception.NotAuthorizedException;
import net.mattcarpenter.performancereview.mapper.EntityToModelMapper;
import net.mattcarpenter.performancereview.model.Token;
import net.mattcarpenter.performancereview.utils.Crypto;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.GeneralSecurityException;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    private EmployeeDao employeeDao;
    private RsaJsonWebKey rsaJsonWebKey;

    private static final String ISSUER = "mattcarpenter.net";
    private static final String AUDIENCE = "paypay-ui";
    private static final String EMAIL_CLAIM = "email";
    private static final String ADMIN_CLAIM = "admin";

    private static int DEFAULT_TOKEN_TTL_SECONDS = 60 * 60;

    public AuthService(EmployeeDao employeeDao) throws Exception {
        this.employeeDao = employeeDao;

        /**
         * To limit the scope of this demo, a new JWK will be created and held in memory every time the service starts up.
         * Key loading, key rotation, etc... will not be implemented.
         */
        rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        rsaJsonWebKey.setKeyId("k1");
    }

    public String login(String emailAddress, String password) {
        return login(emailAddress, password, DEFAULT_TOKEN_TTL_SECONDS);
    }

    public String login(String emailAddress, String password, int tokenTtl) {

        // locate employee record
        EmployeeEntity employee = employeeDao.findByEmailAddress(emailAddress);
        if (employee == null) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_EMAIL);
        }

        // validate password
        try {
            if (!Crypto.validatePassword(password, EntityToModelMapper.mapToCredentialModel(employee.getCredential()))) {
                // global exception handler will translate this to a more generic error code before returning to the client
                throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_PASSWORD);
            }
        } catch (GeneralSecurityException gsx) {
            throw new BadRequestException(ErrorCode.INTERNAL_SERVICE_ERROR);
        }

        // password matches; grant token
        String jwt;
        try {
            jwt = createAccessToken(employee.getId(), employee.getEmailAddress(), employee.isAdmin(), tokenTtl);
        } catch (JoseException je) {
            throw new BadRequestException(ErrorCode.INTERNAL_SERVICE_ERROR);
        }

        return jwt;
    }

    public Token validateJwt(String jwt) {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setAllowedClockSkewInSeconds(1)
                .setRequireSubject()
                .setExpectedIssuer(ISSUER)
                .setExpectedAudience(AUDIENCE)
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(
                        AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256)
                .build();

        JwtClaims claims;
        UUID employeeId = null;
        String jwtId;

        try {
            claims = jwtConsumer.processToClaims(jwt);
            employeeId = UUID.fromString(claims.getSubject());
            jwtId = claims.getJwtId();
        } catch (InvalidJwtException | MalformedClaimException ex) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }

        EmployeeEntity employeeEntity = employeeDao.findById(employeeId).orElseThrow();

        return Token.builder()
                .employeeId(employeeId)
                .isAdmin(employeeEntity.isAdmin())
                .jwtId(jwtId)
                .build();
    }

    public Token loadTokenFromSecurityContext() {
        try {
            return (Token) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception ex) {
            throw new NotAuthorizedException(ErrorCode.AUTHORIZATION_INVALID_OR_EXPIRED_TOKEN);
        }
    }

    private String createAccessToken(UUID employeeId, String emailAddress, boolean isAdmin, int ttl) throws JoseException {
        JwtClaims claims = new JwtClaims();
        NumericDate expiration = NumericDate.now();
        expiration.addSeconds(ttl);

        claims.setIssuer(ISSUER);
        claims.setAudience(AUDIENCE);
        claims.setSubject(employeeId.toString());
        claims.setStringClaim(EMAIL_CLAIM, emailAddress);
        claims.setStringClaim(ADMIN_CLAIM, Boolean.toString(isAdmin));
        claims.setExpirationTime(expiration);
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setGeneratedJwtId();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }
}
