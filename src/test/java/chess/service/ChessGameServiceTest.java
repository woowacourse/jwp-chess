package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.PieceDAO;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import chess.dto.ChessGameDto;
import chess.dto.ChessGameResponseDto;
import chess.dto.ChessGameStatusDto;
import chess.dto.ScoreDto;
import chess.exception.NotFoundPlayingChessGameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@JdbcTest
class ChessGameServiceTest {

    private ChessGameDAO chessGameDAO;
    private PieceDAO pieceDAO;
    private ChessGameService chessGameService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDAO = new PieceDAO(jdbcTemplate);
        chessGameDAO = new ChessGameDAO(jdbcTemplate);
        chessGameService = new ChessGameService(chessGameDAO, pieceDAO);
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM piece");
        jdbcTemplate.execute("DELETE FROM chess_game");
    }

    @DisplayName("새 체스 게임을 생성하는 기능을 테스트한다")
    @Test
    void testCreateNewChessGame() {
        //when
        ChessGameResponseDto chessGameDto = chessGameService.createNewChessGame();

        //then
        assertAll(
                () -> assertThat(chessGameDto).isNotNull(),
                () -> assertThat(chessGameDto.getState()).isEqualTo("BlackTurn"),
                () -> assertThat(chessGameDto.getPieceDtos()).hasSize(32)
        );
    }

    @DisplayName("Piece를 움직이는 기능을 테스트한다")
    @Test
    void testMoveChessPiece() {
        //given
        ChessGameResponseDto chessGameDto = chessGameService.createNewChessGame();

        //when
        chessGameService.moveChessPiece(chessGameDto.getChessGameId(),
                new Position(1, 0), new Position(3, 0));

        //then
        Piece findPiece = pieceDAO.findOneByPosition(chessGameDto.getChessGameId(), 3, 0).get();
        assertThat(findPiece).isNotNull();
    }

    @DisplayName("가장 최근 게임의 상태를 조회하는 기능을 테스트한다")
    @ParameterizedTest
    @CsvSource(value = {
            "WhiteTurn:true", "BlackTurn:true", "End:false", "Ready:false"
    }, delimiter = ':')
    void testFindLatestChessGameStatus(String state, boolean expected) {
        //given
        Long chessGameId = chessGameDAO.save();
        chessGameDAO.updateState(chessGameId, state);

        //when
        ChessGameStatusDto chessGameStatus = chessGameService.findLatestChessGameStatus();

        //then
        assertThat(chessGameStatus.isExist()).isEqualTo(expected);
    }

    @DisplayName("체스 게임을 종료하는 기능을 테스트한다")
    @Test
    void testEndGame() {
        //given
        Long id = chessGameDAO.save();

        //when
        chessGameService.endGame(id);

        //then
        ChessGameStatusDto chessGameStatusDto = chessGameDAO.findIsExistPlayingChessGameStatus();
        assertThat(chessGameStatusDto.isExist()).isFalse();
    }

    @DisplayName("체스 게임의 점수를 계산하는 기능을 테스트한다")
    @Test
    void testCalculateScores() {
        //given
        ChessGameResponseDto chessGame = chessGameService.createNewChessGame();
        pieceDAO.delete(chessGame.getChessGameId(), 1, 0);

        //when
        ScoreDto scoreDto = chessGameService.calculateScores(chessGame.getChessGameId());

        //then
        assertAll(
                () -> assertThat(scoreDto.getBlackScore()).isEqualTo(37.0),
                () -> assertThat(scoreDto.getWhiteScore()).isEqualTo(38.0)
        );
    }

}
