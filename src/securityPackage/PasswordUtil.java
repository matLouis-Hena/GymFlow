package securityPackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65_536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;
    private static final String SEPARATOR = ":";

    private static final SecureRandom secureRandom = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide.");
        }

        byte[] salt = generateSalt();
        byte[] hash = generateHash(plainPassword.toCharArray(), salt);

        return ALGORITHM
                + SEPARATOR
                + ITERATIONS
                + SEPARATOR
                + Base64.getEncoder().encodeToString(salt)
                + SEPARATOR
                + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null || storedPassword.isBlank()) {
            return false;
        }

        if (!isHashedPassword(storedPassword)) {
            return false;
        }

        String[] parts = storedPassword.split(SEPARATOR);

        if (parts.length != 4) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[3]);

            byte[] actualHash = generateHash(
                    plainPassword.toCharArray(),
                    salt,
                    iterations,
                    expectedHash.length * 8
            );

            return MessageDigest.isEqual(expectedHash, actualHash);

        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public static boolean isHashedPassword(String password) {
        return password != null && password.startsWith(ALGORITHM + SEPARATOR);
    }

    public static String hashIfNeeded(String password) {
        if (isHashedPassword(password)) {
            return password;
        }

        return hashPassword(password);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        secureRandom.nextBytes(salt);
        return salt;
    }

    private static byte[] generateHash(char[] password, byte[] salt) {
        return generateHash(password, salt, ITERATIONS, KEY_LENGTH_BITS);
    }

    private static byte[] generateHash(char[] password, byte[] salt, int iterations, int keyLengthBits) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
            throw new IllegalStateException("Erreur lors du hashage du mot de passe.", exception);
        }
    }
}