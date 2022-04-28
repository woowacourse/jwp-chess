package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class HashingTest {

    private static final byte[] SALT = "salt".getBytes();
    private static final String PASSWORD = "password";

    SecretKeyFactory factory;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    @Test
    void 동일한_입력값과_salt에_대한_해쉬값은_동일() throws InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 128);
        byte[] hashByte1 = factory.generateSecret(spec).getEncoded();

        KeySpec spec2 = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 128);
        byte[] hashByte2 = factory.generateSecret(spec2).getEncoded();

        assertThat(hashByte1).isEqualTo(hashByte2);
        // System.out.println(Base64.getEncoder().encodeToString(hashByte1));
    }

    @Test
    void 동일한_입력값에_대해_salt가_다르면_다른_해쉬값_출력() throws InvalidKeySpecException {
        final byte[] SALT1 = "salt1".getBytes();
        final byte[] SALT2 = "salt2".getBytes();

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT1, 65536, 128);
        byte[] hashByte1 = factory.generateSecret(spec).getEncoded();

        KeySpec spec2 = new PBEKeySpec(PASSWORD.toCharArray(), SALT2, 65536, 128);
        byte[] hashByte2 = factory.generateSecret(spec2).getEncoded();

        assertThat(hashByte1).isNotEqualTo(hashByte2);
        // System.out.println(Base64.getEncoder().encodeToString(hashByte1));
        // System.out.println(Base64.getEncoder().encodeToString(hashByte2));
    }

    @Test
    void 동일한_입력값과_salt에_대해_iterationCount가_다르면_다른_해쉬값_출력() throws InvalidKeySpecException {
        int iterationCount1 = 100;
        int iterationCount2 = 65536;

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, iterationCount1, 128);
        byte[] hashByte1 = factory.generateSecret(spec).getEncoded();

        KeySpec spec2 = new PBEKeySpec(PASSWORD.toCharArray(), SALT, iterationCount2, 128);
        byte[] hashByte2 = factory.generateSecret(spec2).getEncoded();

        assertThat(hashByte1).isNotEqualTo(hashByte2);
        // System.out.println(Base64.getEncoder().encodeToString(hashByte1));
        // System.out.println(Base64.getEncoder().encodeToString(hashByte2));
    }

    @Test
    void 동일한_입력값과_salt에_대해_keyLength가_다르면_다른_해쉬값_출력() throws InvalidKeySpecException {
        int keyLength1 = 128;
        int keyLength2 = 256;

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, keyLength1);
        byte[] hashByte1 = factory.generateSecret(spec).getEncoded();

        KeySpec spec2 = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, keyLength2);
        byte[] hashByte2 = factory.generateSecret(spec2).getEncoded();

        assertThat(hashByte1).isNotEqualTo(hashByte2);
        // System.out.println(Base64.getEncoder().encodeToString(hashByte1));
        // System.out.println(Base64.getEncoder().encodeToString(hashByte2));
    }

    @Test
    void hash의_길이는_비트단위로_지정가능() throws InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 128);
        byte[] hashByte128 = factory.generateSecret(spec).getEncoded();

        KeySpec spec2 = new PBEKeySpec(PASSWORD.toCharArray(), SALT, 65536, 256);
        byte[] hashByte256 = factory.generateSecret(spec2).getEncoded();

        assertThat(hashByte128.length).isEqualTo(128 / 8);
        assertThat(hashByte256.length).isEqualTo(256 / 8);
    }
}
