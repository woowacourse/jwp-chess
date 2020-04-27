package wooteco.chess.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.db.MoveHistoryDao;
import wooteco.chess.db.TestPieceDao;
import wooteco.chess.domains.board.BoardFactory;
import wooteco.chess.domains.piece.Piece;
import wooteco.chess.domains.position.Position;

import java.sql.SQLException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ChessWebServiceTest {

    @DisplayName("이어하기 가능 여부 테스트 : 저장된 정보가 체스 보드 칸 64개를 복구할 수 있는 경우")
    @Test
    void test() throws SQLException {
        Map<Position, Piece> boardStatus = BoardFactory.getBoard();
        TestPieceDao dao = new TestPieceDao("guest", boardStatus);
        ChessWebService webService = new ChessWebService(dao, new MoveHistoryDao());

        boolean actual = webService.canResume("guest");

        assertThat(actual).isTrue();
    }


    @DisplayName("이어하기 가능 여부 테스트 : 저장된 정보가 체스 보드 칸 64개를 복구할 수 없는 경우")
    @Test
    void test2() throws SQLException {
        Map<Position, Piece> boardStatus = BoardFactory.getBoard();
        boardStatus.remove(Position.ofPositionName("a1"));
        TestPieceDao dao = new TestPieceDao("guest", boardStatus);
        ChessWebService webService = new ChessWebService(dao, new MoveHistoryDao());

        boolean actual = webService.canResume("guest");

        assertThat(actual).isFalse();
    }

}
