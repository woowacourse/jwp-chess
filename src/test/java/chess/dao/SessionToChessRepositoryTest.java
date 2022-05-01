package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.ChessBoard;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.gameflow.AlternatingGameFlow;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

class SessionToChessRepositoryTest {

    private SessionToChessRepository sessionToChessRepository;

    private ChessBoard chessBoard = new ChessBoard(RegularBoardFactory.getInstance(), new AlternatingGameFlow());

    @BeforeEach
    void setUp() {
        sessionToChessRepository = new SessionToChessRepository();
    }

    @DisplayName("세션을 등록하고 얻을 수 있다")
    @Test
    void session_add_and_get() {
        MockHttpSession session = new MockHttpSession();
        sessionToChessRepository.add(session, chessBoard);
        ChessBoard findChessBoard = sessionToChessRepository.get(session);
        assertThat(findChessBoard).isEqualTo(chessBoard);
    }

    @DisplayName("세션을 등록했다가 지울 수 있다")
    @Test
    void session_and_delete() {
        MockHttpSession session = new MockHttpSession();
        sessionToChessRepository.add(session, chessBoard);
        sessionToChessRepository.delete(session);
        ChessBoard findChessBoard = sessionToChessRepository.get(session);
        Assertions.assertThat(findChessBoard).isNull();
    }
}