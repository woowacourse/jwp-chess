package wooteco.chess.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class PieceInfoRepositoryTest {

    @Autowired
    private PieceInfoRepository pieceInfoRepository;

    @DisplayName("체스 말 정보 저장")
    @Test
    void save() {
        PieceInfo pieceInfo = new PieceInfo("whiterook", "a1");
        pieceInfoRepository.save(pieceInfo);

        PieceInfo persistBoardInfo = pieceInfoRepository.findById(pieceInfo.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(persistBoardInfo.getPiece()).isEqualTo("whiterook");
        assertThat(persistBoardInfo.getPosition()).isEqualTo("a1");
    }
}
