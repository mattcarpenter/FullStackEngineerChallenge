package net.mattcarpenter.performancereview.utils;

import net.mattcarpenter.performancereview.model.CredentialModel;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.*;

public class CryptoTests {

    private static final String TEST_PASSWORD_1 = "tastycakes";
    private static final String TEST_PASSWORD_2 = "dancingpizzahotel";
    private static final String TEST_PASSWORD_3 = "burrito";
    private static final String TEST_STORED_PASSWORD_HASH = "a9c6a6943c17e0d6629f47d30d2c6699fb0efeda689b94247849fecd6b47b3e0258541d29f360b29a8b95fc53b1be5748ea5dfebb2455c5115f529534c93f962";
    private static final String TEST_STORED_PASSWORD_SALT = "1e6461ec6e6099618456f81c49a536a1";

    @Test
    public void validatePassword_validatesCorrectPassword() throws Exception {
        CredentialModel credential = Crypto.generatePasswordHashAndSalt(TEST_PASSWORD_1);
        assertThat(Crypto.validatePassword(TEST_PASSWORD_1, credential)).isTrue();
    }

    @Test
    public void validatePasword_incorrectPassword() throws Exception {
        CredentialModel credential = Crypto.generatePasswordHashAndSalt(TEST_PASSWORD_1);
        assertThat(Crypto.validatePassword(TEST_PASSWORD_2, credential)).isFalse();
    }

    @Test
    public void validatePassword_clobberedHash() throws Exception {
        CredentialModel credential = Crypto.generatePasswordHashAndSalt(TEST_PASSWORD_1);
        credential.setPassword("borked");
        assertThatThrownBy(() -> { Crypto.validatePassword(TEST_PASSWORD_1, credential); });
    }

    @Test
    public void validatePassword_clobberedSalt() throws Exception {
        CredentialModel credential = Crypto.generatePasswordHashAndSalt(TEST_PASSWORD_1);
        credential.setPasswordSalt("salty");
        assertThatThrownBy(() -> { Crypto.validatePassword(TEST_PASSWORD_1, credential); });
    }

    @Test
    public void validatePassword_validatesStoredPassword() throws Exception {
        CredentialModel credential = new CredentialModel(TEST_STORED_PASSWORD_HASH, TEST_STORED_PASSWORD_SALT, "", 1000);
        assertThat(Crypto.validatePassword(TEST_PASSWORD_3, credential)).isTrue();
    }

    @Test
    public void validatePassword_blankPassword() throws Exception {
        CredentialModel credential = Crypto.generatePasswordHashAndSalt(TEST_PASSWORD_1);
        assertThat(Crypto.validatePassword("", credential)).isFalse();
    }
}
