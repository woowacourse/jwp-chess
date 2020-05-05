package wooteco.chess.domain.position;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class FileTest {
    @Test
    void getFileDifferenceTest() {
        assertThat(File.H.getFileDifference(File.A)).isEqualTo(-7);
    }
}
