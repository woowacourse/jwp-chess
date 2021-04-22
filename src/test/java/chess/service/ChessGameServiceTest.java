package chess.service;

import chess.dao.ChessGameDAO;
import chess.dao.PieceDAO;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import chess.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @DisplayName("새 체스 게임을 생성하는 기능을 테스트한다")
    @Test
    void testCreateNewChessGame() {
        //when
        ChessGameInfoResponseDto chessGameDto = chessGameService.createNewChessGame("title");

        //then
        assertAll(
                () -> assertThat(chessGameDto).isNotNull(),
                () -> assertThat(chessGameDto.getState()).isEqualTo("Ready"),
                () -> assertThat(chessGameDto.getPieceDtos()).hasSize(32)
        );
    }

    @DisplayName("Piece를 움직이는 기능을 테스트한다")
    @Test
    void testMoveChessPiece() {
        //given
        ChessGameInfoResponseDto chessGameDto = chessGameService.createNewChessGame("title");
        chessGameDAO.updateState(chessGameDto.getChessGameId(), "BlackTurn");

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
        Long chessGameId = chessGameDAO.save("title");
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
        Long id = chessGameDAO.save("title");
        chessGameDAO.updateState(id, "BlackTurn");

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
        ChessGameInfoResponseDto chessGame = chessGameService.createNewChessGame("title");
        pieceDAO.delete(chessGame.getChessGameId(), 1, 0);

        //when
        ScoreDto scoreDto = chessGameService.calculateScores(chessGame.getChessGameId());

        //then
        assertAll(
                () -> assertThat(scoreDto.getBlackScore()).isEqualTo(37.0),
                () -> assertThat(scoreDto.getWhiteScore()).isEqualTo(38.0)
        );
    }

    @DisplayName("id로 특정 chessgame을 조회하는 기능을 테스트한다")
    @Test
    void testFindChessGameById() {
        //given
        Long id = chessGameDAO.save("title");

        //when
        ChessGameResponseDto chessGameResponseDto = chessGameService.findChessGameById(id);

        //then
        assertAll(
                () -> assertThat(chessGameResponseDto).isNotNull(),
                () -> assertThat(chessGameResponseDto.getState()).isEqualTo("Ready"),
                () -> assertThat(chessGameResponseDto.getPieceDtos()).isEmpty()
        );
    }

    @DisplayName("체스게임 아이디로 특정 체스게임의 정보를 조회환다 ")
    @Test
    void testFindChessGameInfoById() {
        //given
        String title = "title";
        Long id = chessGameDAO.save(title);

        //when
        ChessGameInfoResponseDto chessGameInfoResponseDto = chessGameService.findChessGameInfoById(id);

        //then
        assertAll(
                () -> assertThat(chessGameInfoResponseDto.getChessGameId()).isEqualTo(id),
                () -> assertThat(chessGameInfoResponseDto.getState()).isEqualTo("Ready"),
                () -> assertThat(chessGameInfoResponseDto.getTitle()).isEqualTo(title),
                () -> assertThat(chessGameInfoResponseDto.getPieceDtos()).isEmpty(),
                () -> assertThat(chessGameInfoResponseDto.isEnd()).isFalse(),
                () -> assertThat(chessGameInfoResponseDto.isReady()).isTrue(),
                () -> assertThat(chessGameInfoResponseDto.isPlaying()).isFalse(),
                () -> assertThat(chessGameInfoResponseDto.isFinished()).isFalse()
        );
    }

    @DisplayName("실행중인 모든 게임을 찾는 기능을 테스트한다")
    @Test
    void testFindAllPlayingGames() {
        //given
        Long firstId = chessGameDAO.save("title1");
        Long secondId = chessGameDAO.save("title2");
        Long thirdId = chessGameDAO.save("title3");
        chessGameDAO.updateState(secondId,"End");
        chessGameDAO.updateState(thirdId,"BlackTurn");

        //when
        List<PlayingChessgameEntityDto> allPlayingGames = chessGameService.findAllPlayingGames();

        //then
        assertAll(
                () -> assertThat(allPlayingGames).hasSize(2),
                () -> assertThat(allPlayingGames).extracting("chessGameId")
                        .containsAnyOf(firstId, thirdId)
        );
    }

}
