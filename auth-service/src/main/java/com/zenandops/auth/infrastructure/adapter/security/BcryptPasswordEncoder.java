package com.zenandops.auth.infrastructure.adapter.security;

import com.zenandops.auth.application.port.PasswordEncoder;
import jakarta.enterprise.context.ApplicationScoped;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.spec.EncryptablePasswordSpec;
import org.wildfly.security.password.spec.IteratedSaltedPasswordAlgorithmSpec;
import org.wildfly.security.password.util.ModularCrypt;

import java.security.SecureRandom;

/**
 * Bcrypt adapter implementing the PasswordEncoder port using Quarkus Elytron security.
 */
@ApplicationScoped
public class BcryptPasswordEncoder implements PasswordEncoder {

    private static final int ITERATION_COUNT = 10;
    private static final int SALT_LENGTH = 16;

    @Override
    public String encode(String rawPassword) {
        try {
            byte[] salt = new byte[SALT_LENGTH];
            new SecureRandom().nextBytes(salt);

            PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            IteratedSaltedPasswordAlgorithmSpec spec =
                    new IteratedSaltedPasswordAlgorithmSpec(ITERATION_COUNT, salt);
            EncryptablePasswordSpec encryptableSpec =
                    new EncryptablePasswordSpec(rawPassword.toCharArray(), spec);

            BCryptPassword bcryptPassword = (BCryptPassword) factory.generatePassword(encryptableSpec);
            return ModularCrypt.encodeAsString(bcryptPassword);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode password", e);
        }
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            Password password = ModularCrypt.decode(encodedPassword);
            Password restored = factory.translate(password);
            return factory.verify(restored, rawPassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify password", e);
        }
    }
}
