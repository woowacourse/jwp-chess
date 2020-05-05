package wooteco.chess.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.repository.ChessEntity;
import wooteco.chess.repository.ChessRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class BoardServiceTest {
    private static final String INIT_BOARD_INFORMATION = "rnbqkbnrpppppppp................................PPPPPPPPRNBQKBNR";

    @Autowired
    private BoardService boardService;

    @Autowired
    private ChessRepository chessRepository;

    @BeforeEach
    void setUp() {
        chessRepository.deleteAll();
        chessRepository.initTable();
    }

    @DisplayName("존재하지 않는 방 id를 접근하려고 할때 예외를 발생시키는지 테스트")
    @Test
    void playTest() {
        chessRepository.save(new ChessEntity("cherry", INIT_BOARD_INFORMATION, true));
        assertThatThrownBy(() -> boardService.play(2L, "a1", "a2"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id와 일치하는 Room이 없습니다.");
    }

    @AfterEach
    void tearDown() {
        chessRepository.deleteAll();
    }

}
