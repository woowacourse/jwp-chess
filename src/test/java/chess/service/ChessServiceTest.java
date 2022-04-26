package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.Command;
import chess.dto.ChessGameDto;

@SpringBootTest
@Transactional
class ChessServiceTest {
    private ChessService chessService;
    private int savedId;

    // Fake 객체로 바꾸기
    @Autowired
    private ChessGameDao chessGameDao;
    @Autowired
    private PieceDao pieceDao;

    @BeforeEach
    void setUp() {
        chessService = new ChessService(chessGameDao, pieceDao);
        savedId = chessService.save("test_game1", "1234");
        chessService.save("test_game2", "1234");
        chessService.save("test_game3", "1234");
    }

    @Test
    @DisplayName("게임을 한개 더 생성하면 4개가 되어야 합니다.")
    void testGamesSize() {
        chessService.save("test_game4", "1234");
        List<ChessGameDto> games = chessService.findAllChessGames();
        assertThat(games).hasSize(4);
    }

    @Test
    @DisplayName("게임을 생성하면 말의 개수가 32개여야 합니다.")
    void testPiecesOfGames() {
        int savedId = chessService.save("test_game4", "1234");
        List<String> chessBoard = chessService.getCurrentChessBoard(savedId);
        List<String> existPieces = chessBoard.stream()
            .filter(piece -> !piece.equals(""))
            .collect(Collectors.toList());

        assertThat(existPieces).hasSize(32);
    }

    @Test
    @DisplayName("게임을 종료하면 게임의 상태는 종료상태여야 합니다.")
    void end() {
        chessService.finish(Command.from("end"), savedId);
        assertThat(chessService.isEnd(savedId)).isTrue();
    }
}