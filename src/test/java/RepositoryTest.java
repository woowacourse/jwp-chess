import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wooteco.chess.repository.ChessEntity;
import wooteco.chess.repository.ChessRepository;

import java.util.Optional;

public class RepositoryTest {
    private ChessRepository chessRepository;
    @BeforeEach
    void setUp() {
        chessRepository = new
    }

    @Test
    void findByIdTest() {
        Optional<ChessEntity> entity = chessRepository.findById(roomId);

    }
}
