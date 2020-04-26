package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PieceRepositoryTest {

    @Test
    @DisplayName("pieces 생성 테스트")
    void create() {
        assertThat(PieceRepository.pieces()).isInstanceOf(List.class);
    }

    @Test
    @DisplayName("Blank 생성 테스트")
    void getBlank() {
        assertThat(PieceRepository.getBlank()).isInstanceOf(Blank.class);
    }
}
