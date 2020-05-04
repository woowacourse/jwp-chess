package wooteco.chess.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ChessRepositoryTest {
    private static String INIT_BOARD_INFORMATION = "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR";
    private static String MODIFIED_BOARD_INFORMATION = "rnbpppppp............PPPPPP......qkbnrpp......PPRNB........QKBNR";

    @Autowired
    private ChessRepository chessRepository;

    @BeforeEach
    void setUp() {
        chessRepository.deleteAll();
        chessRepository.initTable();
    }

    @Test
    void createTest() {
        ChessEntity savedEntity = chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        assertThat(savedEntity).isNotNull();
    }

    @Test
    void findAllIdsTest() {
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));

        assertThat(chessRepository.findAllIds().size()).isEqualTo(3);
    }

    @Test
    void findByIdTest() {
        ChessEntity savedEntity = chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        ChessEntity entity = chessRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        assertThat(savedEntity).isEqualTo(entity);
    }

    @Test
    void saveTest() {
        ChessEntity savedEntity = chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        assertThat(savedEntity.getBoard()).isEqualTo(INIT_BOARD_INFORMATION);
    }

    @Test
    void updateTest() {
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        chessRepository.update(1L, MODIFIED_BOARD_INFORMATION, false);
        ChessEntity updatedEntity = chessRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
        assertThat(updatedEntity.getBoard()).isEqualTo(MODIFIED_BOARD_INFORMATION);
    }

    @Test
    void findTitleByIdTest() {
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        assertThat(chessRepository.findTitleById(1L)).isEqualTo("cherry");
    }

    @AfterEach
    void tearDown() {
        chessRepository.deleteAll();
    }
}
