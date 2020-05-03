package wooteco.chess.domain.gameinfo;

import static org.assertj.core.api.Assertions.*;
import static wooteco.chess.domain.player.PlayerColor.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.exception.InvalidMovementException;
import wooteco.chess.domain.piece.GamePiece;
import wooteco.chess.domain.piece.King;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Rook;

class GameInfoTest {

    @ParameterizedTest
    @DisplayName("자기턴이 아닌 때 움직이려고 하는 경우")
    @MethodSource("createBoardAndTurn")
    void moveWhenInvalidTurn(int turn, GamePiece gamePiece) {
        Map<Position, GamePiece> map = new HashMap<>(
            BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from("d5"), gamePiece);
        Board board = BoardFactory.of(map);
        GameInfo gameInfo = GameInfo.from(board, turn);

        assertThatThrownBy(() -> {
            gameInfo.move("d5", "g8");
        }).isInstanceOf(InvalidMovementException.class)
            .hasMessage("이동할 수 없습니다.\n해당 플레이어의 턴이 아닙니다.");
    }

    static Stream<Arguments> createBoardAndTurn() {
        return Stream.of(
            Arguments.of(1, new Rook(WHITE)),
            Arguments.of(0, new Rook(BLACK))
        );
    }

    @ParameterizedTest
    @DisplayName("Board가 Finish 여부 확인")
    @MethodSource("createFinish")
    void isBoardFinished(String source, String target, boolean expected) {
        Map<Position, GamePiece> map = new HashMap<>(
            BoardFactory.EMPTY_BOARD.getBoard());
        map.put(Position.from("c5"), new King(WHITE));
        map.put(Position.from("d6"), new Pawn(BLACK));
        GameInfo gameInfo = GameInfo.from(BoardFactory.of(map), 1);
        gameInfo = gameInfo.move(source, target);

        assertThat(gameInfo.getStatus().isNotFinished()).isEqualTo(expected);
    }

    static Stream<Arguments> createFinish() {
        return Stream.of(
            Arguments.of("d6", "d5", true),
            Arguments.of("d6", "c5", false)
        );
    }
}