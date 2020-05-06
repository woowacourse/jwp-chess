package wooteco.chess.database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.chess.repository.CommandRepository;
import wooteco.chess.dto.Commands;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringCommandsDaoTest {
    @Autowired
    CommandRepository commandRepository;

    @BeforeEach
    void setUp() {
        commandRepository.save(new Commands("move a2 a4"));
        commandRepository.save(new Commands("move a7 a5"));
    }

    @AfterEach
    void afterEach() {
        commandRepository.deleteAll();
    }

    @DisplayName("테이블 행 삭제")
    @Test
    void clearCommands() {
        commandRepository.deleteAll();

        assertThat(commandRepository.findAll().size()).isZero();
    }

    @DisplayName("전체 행 선택")
    @Test
    void selectCommands() {
        assertThat(commandRepository.findAll().size()).isEqualTo(2);
    }
}