package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.domain.ChessGame;
import chess.domain.Command;
import chess.domain.state.State;
import chess.dto.ChessGameDto;
import java.sql.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ChessGameDaoTest {

    private static final String PASSWORD = "password";
    private static final String GAME_NAME = "test";
    @Autowired
    private ChessGameDao chessGameDao;
    @Autowired
    private PieceDao pieceDao;

    @AfterEach
    void rollback() {
        chessGameDao.remove(GAME_NAME);
    }

    @DisplayName("커넥션 테스트")
    @Test
    public void connection() {
        //given & when
        Connection connection = DBConnector.getConnection();

        //then
        assertThat(connection).isNotNull();
    }

    @DisplayName("id로 체스게임 찾기")
    @Test
    public void select() {
        //given
        ChessGame chessGame = new ChessGame(GAME_NAME);
        chessGame.progress(Command.from("start"));

        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        //when
        Long id = chessGameDao.save(chessGameDto, PASSWORD);

        //then
        assertThat(chessGameDao.findIdByGameName(GAME_NAME)).isEqualTo(id);
    }

    @DisplayName("게임 이름과 비밀번호로 상태 찾기")
    @Test
    public void findState() {
        //given
        ChessGame chessGame = new ChessGame(GAME_NAME);
        chessGame.progress(Command.from("start"));

        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        //when
        Long id = chessGameDao.save(chessGameDto, PASSWORD);

        //then
        assertThat(chessGameDao.findStateByGameNameAndPassword(GAME_NAME, PASSWORD).getTurn()).isEqualTo("white");
    }
    
    @DisplayName("체스 게임 저장 테스트")
    @Test
    public void save() {
        //given
        ChessGame chessGame = new ChessGame(GAME_NAME);
        chessGame.progress(Command.from("start"));

        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        //when & then
        assertThatThrownBy(() -> chessGameDao.findIdByGameName(GAME_NAME)).isInstanceOf(
                EmptyResultDataAccessException.class);
        assertDoesNotThrow(() -> chessGameDao.save(chessGameDto, PASSWORD));
    }

    @DisplayName("체스 게임 업데이트 테스트")
    @Test
    public void update() {
        //given
        ChessGame chessGame = new ChessGame(GAME_NAME);
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        Long id = chessGameDao.save(chessGameDto, PASSWORD);
        pieceDao.save(id, chessGameDto);

        //when
        chessGame.progress(Command.from("start"));
        chessGameDto = ChessGameDto.from(chessGame);

        chessGameDao.update(id, chessGameDto);

        //then
        Long savedId = chessGameDao.findIdByGameName(GAME_NAME);
        chessGame = chessGameDao.findById(savedId);

        State state = chessGame.getState();
        assertThat(state.getTurn()).isEqualTo("white");
    }

    @DisplayName("종료 상태인 게임 삭제하기")
    @Test
    public void delete() {
        //given
        ChessGame chessGame = new ChessGame(GAME_NAME);
        chessGame.progress(Command.from("start"));
        chessGame.progress(Command.from("end"));
        ChessGameDto chessGameDto = ChessGameDto.from(chessGame);

        Long id = chessGameDao.save(chessGameDto, PASSWORD);
        pieceDao.save(id, chessGameDto);

        //when
        chessGameDao.deleteByGameNameAndPassword(GAME_NAME, PASSWORD);

        //then
        assertThat(chessGameDao.findById(id)).isNull();
    }
}
