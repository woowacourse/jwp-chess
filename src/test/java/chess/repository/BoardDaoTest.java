package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.repository.entity.BoardEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/settingForTest.sql")
public class BoardDaoTest {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private BoardDao boardDao;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDao(namedParameterJdbcTemplate);
    }

    @Test
    @DisplayName("BoardEntity 로 board table 에 저장한다.")
    void save() {
        BoardEntity newBoardEntity = new BoardEntity(
                "1111",
                "f",
                2,
                "BISHOP",
                "WHITE"
        );

        boardDao.save(List.of(newBoardEntity));

        assertThat(boardDao.load("1111").size()).isEqualTo(3);
    }

    @Test
    @DisplayName("gameRoomId 로 board table 데이터를 조회한다.")
    void load() {
        assertThat(boardDao.load("1111").size()).isEqualTo(2);
    }

    @Test
    @DisplayName("조회할 board table 데이터가 존재하지 않을 때 예외를 발생시킨다.")
    void validateBoardExist() {
        assertThatThrownBy(() -> boardDao.load("3333"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] Board 가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("BoardEntity 를 이용해서 board table 데이터를 업데이트 한다")
    void updatePiece() {
        BoardEntity newBoardEntity = new BoardEntity(
                "2222",
                "e",
                4,
                "BISHOP",
                "BLACK"
        );

        boardDao.updateBoard(newBoardEntity);

        List<BoardEntity> loadedBoardEntities = boardDao.load("2222");
        assertAll(
                () -> assertThat(loadedBoardEntities.size()).isEqualTo(1),
                () -> assertThat(loadedBoardEntities.get(0).getPieceName()).isEqualTo("BISHOP"),
                () -> assertThat(loadedBoardEntities.get(0).getPieceTeamValue()).isEqualTo("BLACK")
        );
    }
}