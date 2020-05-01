package wooteco.chess.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class ChessRepositoryTest {
    @Autowired
    private ChessRepository chessRepository;
    
    private ChessEntity entity;

    @BeforeEach
    void setUp() {
        chessRepository.deleteAll();
        entity = new ChessEntity(4325L, "cherry", "rnbqkbnrppppp..............ppp...............PPPPPPPPRNBQKBNR",true);
    }
    
    @Test
    void createTest() {
        ChessEntity savedEntity = chessRepository.save(entity);
        assertThat(savedEntity).isNotNull();
    }

//    @Test
//    void readTest() {
//        ChessEntity savedEntity = chessRepository.save(entity);
//        ChessEntity entity = chessRepository.findById(4325L)
//                .orElseThrow(() -> new IllegalArgumentException("Id와 일치하는 Room이 없습니다."));
//        assertThat(savedEntity).isEqualTo(entity);
//    }
}
