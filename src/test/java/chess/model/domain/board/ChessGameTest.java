package chess.model.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.domain.piece.King;
import chess.model.domain.piece.Knight;
import chess.model.domain.piece.Pawn;
import chess.model.domain.piece.Piece;
import chess.model.domain.piece.Rook;
import chess.model.domain.piece.Team;
import chess.model.domain.piece.Type;
import chess.model.domain.state.MoveInfo;
import chess.model.domain.state.MoveState;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ChessGameTest {

    @DisplayName("체스보드 생성시 32개의 칸-말 셋트를 가지고 있는지 확인")
    @Test
    void chessBoardSizeCheck() {
        ChessGame chessGame = new ChessGame();
        Map<Square, Piece> board = chessGame.getChessBoard();
        assertThat(board.size()).isEqualTo(32);
    }

    @DisplayName("move 수행이 가능한지 판단하면서 수행, 턴 변경시 수행 불가능한지도 검증")
    @Test
    void canMove() {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("a7", "a6")).isSucceed())
            .isFalse();
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("a2", "a3")).isSucceed())
            .isTrue();
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("a7", "a6")).isSucceed())
            .isTrue();
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("a7", "b1")).isSucceed())
            .isFalse();
    }

    @Test
    @DisplayName("move 수행 테스트")
    void move() {
        ChessGame chessGame = new ChessGame();
        chessGame.movePieceWhenCanMove(new MoveInfo("a2", "a3"));
        assertThat(chessGame.getChessBoard().containsKey(Square.of("a2"))).isFalse();
        assertThat(chessGame.getChessBoard().containsKey(Square.of("a3"))).isTrue();
        assertThat(chessGame.getChessBoard().get(Square.of("a3")))
            .isEqualTo(Pawn.getPieceInstance(Team.WHITE));
    }

    @Test
    @DisplayName("king 잡혔는지 확인")
    void isKingOnChessBoard() {
        ChessGame chessGame = new ChessGame();
        chessGame.movePieceWhenCanMove(new MoveInfo("e2", "e4"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a7", "a5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("e1", "e2"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a8", "a6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("e2", "e3"));

        assertThat(chessGame.isKingCaptured()).isFalse();

        chessGame.movePieceWhenCanMove(new MoveInfo("a6", "d6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("e3", "d3"));
        chessGame.movePieceWhenCanMove(new MoveInfo("d6", "d3"));

        assertThat(chessGame.isKingCaptured()).isTrue();
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("d1", "d2")))
            .isEqualTo(MoveState.KING_CAPTURED);
    }

    @DisplayName("이동하려는 before자리에 말이 없는건지 확인")
    @Test
    void isNoPiece() {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.isNoPiece(new MoveInfo("a2", "a3"))).isFalse();
        assertThat(chessGame.isNoPiece(new MoveInfo("a3", "a4"))).isTrue();
    }

    @DisplayName("이동하려는 before자리의 말이 현재 차례의 말이 아닌지 확인, 말이 없는 경우도 True")
    @Test
    void isNotMyTurn() {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.isNotMyTurn(new MoveInfo("a2", "a3"))).isFalse();
        assertThat(chessGame.isNotMyTurn(new MoveInfo("a3", "a4"))).isTrue();
        assertThat(chessGame.isNotMyTurn(new MoveInfo("a7", "a6"))).isTrue();
    }

    @DisplayName("폰이 시작지점(즉 상대방의 시작지점)으로 이동했는지 확인")
    @Test
    void mustChangePawnThenCanGo() {
        ChessGame chessGame = new ChessGame();
        chessGame.movePieceWhenCanMove(new MoveInfo("a2", "a4"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a7", "a5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b2", "b4"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b7", "b5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a4", "b5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c7", "c6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b5", "c6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c8", "b7"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c6", "b7"));
        chessGame.movePieceWhenCanMove(new MoveInfo("h7", "h6"));
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("b7", "a8")))
            .isEqualTo(MoveState.SUCCESS_BUT_PAWN_PROMOTION);
    }

    @DisplayName("폰이 시작지점(즉 상대방의 시작지점)으로 이동했을 때, 변경 여부에 따른 진행 여부")
    @Test
    void mustChangePawnAndCanGo() {
        ChessGame chessGame = new ChessGame();
        chessGame.movePieceWhenCanMove(new MoveInfo("a2", "a4"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a7", "a5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b2", "b4"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b7", "b5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("a4", "b5"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c7", "c6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b5", "c6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c8", "b7"));
        chessGame.movePieceWhenCanMove(new MoveInfo("c6", "b7"));
        chessGame.movePieceWhenCanMove(new MoveInfo("h7", "h6"));
        chessGame.movePieceWhenCanMove(new MoveInfo("b7", "a8"));
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("g7", "g6")))
            .isEqualTo(MoveState.FAIL_NOT_ORDER);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("g2", "g3")))
            .isEqualTo(MoveState.FAIL_MUST_PAWN_PROMOTION);
        assertThat(chessGame.promotion(Type.BISHOP)).isEqualTo(MoveState.SUCCESS_PROMOTION);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("g2", "g3")))
            .isEqualTo(MoveState.FAIL_NOT_ORDER);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo("g7", "g6")))
            .isEqualTo(MoveState.SUCCESS);
    }

    @DisplayName("캐슬링 되는지 확인")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castling(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getPieceInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(new BoardInitialTestUse(boardInitial), Team.WHITE,
            CastlingSetting.getCastlingElements(), new EnPassant());

        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.SUCCESS);
    }

    @DisplayName("중간에 장애물이 있는 경우 캐슬링 불가능한지 확인")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castlingNo(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("d1"), Knight.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("d8"), Knight.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("f1"), Knight.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("f8"), Knight.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("a2"), Pawn.getPieceInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(new BoardInitialTestUse(boardInitial), Team.WHITE,
            CastlingSetting.getCastlingElements(), new EnPassant());

        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
        assertThat(chessGame
            .movePieceWhenCanMove(new MoveInfo(Square.of("a2"), Square.of("a3"))))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
    }

    @DisplayName("캐슬링 안되는지 확인 - 캐슬링요소 없을 경우")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castlingNo2(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("d1"), Knight.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("d8"), Knight.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("f1"), Knight.getPieceInstance(Team.BLACK));
        boardInitial.put(Square.of("f8"), Knight.getPieceInstance(Team.WHITE));
        boardInitial.put(Square.of("a2"), Pawn.getPieceInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(new BoardInitialTestUse(boardInitial), Team.WHITE,
            new HashSet<>(Collections.singletonList(CastlingSetting.BLACK_KING_BEFORE)), new EnPassant());

        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
        assertThat(chessGame
            .movePieceWhenCanMove(new MoveInfo(Square.of("a2"), Square.of("a3"))))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.movePieceWhenCanMove(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
    }

}
