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
public class GameStatusRepositoryTest {

    @Autowired
    private GameStatusRepository gameStatusRepository;

    @DisplayName("게임 상태 저장")
    @Test
    void save() {
        GameStatus gameStatus = new GameStatus("white");
        gameStatusRepository.save(gameStatus);

        GameStatus foundGameStatus = gameStatusRepository.findById(gameStatus.getId())
                .orElseThrow(RuntimeException::new);

        assertThat(foundGameStatus.getCurrentTurn()).isEqualTo("white");
    }
}
