package wooteco.chess.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class CommandsRepositoryTest {
    @Autowired
    CommandsRepository commandsRepository;

    @BeforeEach
    void setUp() {
        commandsRepository.save(new Commands("move a2 a4"));
        commandsRepository.save(new Commands("move a7 a5"));
    }

    @AfterEach
    void afterEach() {
        commandsRepository.deleteAll();
    }

    @DisplayName("테이블 행 삭제")
    @Test
    void clearCommands() {
        commandsRepository.deleteAll();

        assertThat(commandsRepository.findAll().size()).isZero();
    }

    @DisplayName("전체 행 선택")
    @Test
    void selectCommands() {
        assertThat(commandsRepository.findAll().size()).isEqualTo(2);
    }
}