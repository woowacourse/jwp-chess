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
        assertThat(chessGame.move(new MoveInfo("a7", "a6")).isSucceed())
            .isFalse();
        assertThat(chessGame.move(new MoveInfo("a2", "a3")).isSucceed())
            .isTrue();
        assertThat(chessGame.move(new MoveInfo("a7", "a6")).isSucceed())
            .isTrue();
        assertThat(chessGame.move(new MoveInfo("a7", "b1")).isSucceed())
            .isFalse();
    }

    @Test
    @DisplayName("move 수행 테스트")
    void move() {
        ChessGame chessGame = new ChessGame();
        chessGame.move(new MoveInfo("a2", "a3"));
        assertThat(chessGame.getChessBoard().containsKey(Square.of("a2"))).isFalse();
        assertThat(chessGame.getChessBoard().containsKey(Square.of("a3"))).isTrue();
        assertThat(chessGame.getChessBoard().get(Square.of("a3")))
            .isEqualTo(Pawn.getInstance(Team.WHITE));
    }

    @Test
    @DisplayName("king 잡혔는지 확인")
    void isKingOnChessBoard() {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.isKingCaptured()).isFalse();

        Map<Square, Piece> whiteKingBoard = new HashMap<>();
        whiteKingBoard.put(Square.of("a1"), King.getInstance(Team.WHITE));
        chessGame = new ChessGame(ChessBoard.of(whiteKingBoard), Team.BLACK,
            CastlingElement.of(new HashSet<>()), EnPassant.createEmpty());
        assertThat(chessGame.isKingCaptured()).isTrue();
        assertThat(chessGame.move(new MoveInfo("d1", "d2")))
            .isEqualTo(MoveState.KING_CAPTURED);

        Map<Square, Piece> blackKingBoard = new HashMap<>();
        blackKingBoard.put(Square.of("a1"), King.getInstance(Team.BLACK));
        chessGame = new ChessGame(ChessBoard.of(blackKingBoard), Team.WHITE,
            CastlingElement.of(new HashSet<>()), EnPassant.createEmpty());
        assertThat(chessGame.isKingCaptured()).isTrue();
        assertThat(chessGame.move(new MoveInfo("d1", "d2")))
            .isEqualTo(MoveState.KING_CAPTURED);
    }

    @DisplayName("칸에 말이 없는건지 확인")
    @ParameterizedTest
    @CsvSource(value = {"a2, false", "a3, true"})
    void isNoPiece(String source, boolean success) {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.isNotExistPiece(Square.of(source)) == success).isTrue();
    }

    @DisplayName("이동하려는 before자리의 말이 현재 차례의 말이 아닌지 확인")
    @Test
    void isNotMyTurn() {
        ChessGame chessGame = new ChessGame();
        assertThat(chessGame.isNotCorrectTurn(new MoveInfo("a2", "a3"))).isFalse();
        assertThat(chessGame.isNotCorrectTurn(new MoveInfo("a7", "a6"))).isTrue();
    }

    @DisplayName("폰이 시작지점(즉 상대방의 시작지점)으로 이동했는지 확인")
    @Test
    void mustChangePawnThenCanGo() {
        ChessGame chessGame = new ChessGame();
        chessGame.move(new MoveInfo("a2", "a4"));
        chessGame.move(new MoveInfo("a7", "a5"));
        chessGame.move(new MoveInfo("b2", "b4"));
        chessGame.move(new MoveInfo("b7", "b5"));
        chessGame.move(new MoveInfo("a4", "b5"));
        chessGame.move(new MoveInfo("c7", "c6"));
        chessGame.move(new MoveInfo("b5", "c6"));
        chessGame.move(new MoveInfo("c8", "b7"));
        chessGame.move(new MoveInfo("c6", "b7"));
        chessGame.move(new MoveInfo("h7", "h6"));
        assertThat(chessGame.move(new MoveInfo("b7", "a8")))
            .isEqualTo(MoveState.SUCCESS_BUT_PAWN_PROMOTION);
    }

    @DisplayName("폰이 시작지점(즉 상대방의 시작지점)으로 이동했을 때, 변경 여부에 따른 진행 여부")
    @Test
    void mustChangePawnAndCanGo() {
        ChessGame chessGame = new ChessGame();
        chessGame.move(new MoveInfo("a2", "a4"));
        chessGame.move(new MoveInfo("a7", "a5"));
        chessGame.move(new MoveInfo("b2", "b4"));
        chessGame.move(new MoveInfo("b7", "b5"));
        chessGame.move(new MoveInfo("a4", "b5"));
        chessGame.move(new MoveInfo("c7", "c6"));
        chessGame.move(new MoveInfo("b5", "c6"));
        chessGame.move(new MoveInfo("c8", "b7"));
        chessGame.move(new MoveInfo("c6", "b7"));
        chessGame.move(new MoveInfo("h7", "h6"));
        chessGame.move(new MoveInfo("b7", "a8"));
        assertThat(chessGame.move(new MoveInfo("g7", "g6")))
            .isEqualTo(MoveState.FAIL_NOT_ORDER);
        assertThat(chessGame.move(new MoveInfo("g2", "g3")))
            .isEqualTo(MoveState.FAIL_MUST_PAWN_PROMOTION);
        assertThat(chessGame.promote(Type.BISHOP)).isEqualTo(MoveState.SUCCESS_PROMOTION);
        assertThat(chessGame.move(new MoveInfo("g2", "g3")))
            .isEqualTo(MoveState.FAIL_NOT_ORDER);
        assertThat(chessGame.move(new MoveInfo("g7", "g6")))
            .isEqualTo(MoveState.SUCCESS);
    }

    @DisplayName("캐슬링 되는지 확인")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castling(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(ChessBoard.of(boardInitial), Team.WHITE,
            CastlingElement.createInitial(), EnPassant.createEmpty());

        assertThat(chessGame.move(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.move(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.SUCCESS);
    }

    @DisplayName("중간에 장애물이 있는 경우 캐슬링 불가능한지 확인")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castlingNo(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getInstance(Team.WHITE));
        boardInitial.put(Square.of("d1"), Knight.getInstance(Team.BLACK));
        boardInitial.put(Square.of("d8"), Knight.getInstance(Team.WHITE));
        boardInitial.put(Square.of("f1"), Knight.getInstance(Team.BLACK));
        boardInitial.put(Square.of("f8"), Knight.getInstance(Team.WHITE));
        boardInitial.put(Square.of("a2"), Pawn.getInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(ChessBoard.of(boardInitial), Team.WHITE,
            CastlingElement.createInitial(), EnPassant.createEmpty());

        assertThat(chessGame.move(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
        assertThat(chessGame
            .move(new MoveInfo(Square.of("a2"), Square.of("a3"))))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.move(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
    }

    @DisplayName("캐슬링 안되는지 확인 - 캐슬링요소 없을 경우")
    @ParameterizedTest
    @CsvSource(value = {"e1, c1, e8, c8", "e1, g1, e8, g8"})
    void castlingNo2(String whiteBefore, String whiteAfter, String blackBefore, String blackAfter) {
        Map<Square, Piece> boardInitial = new HashMap<>();
        boardInitial.put(Square.of("e1"), King.getInstance(Team.WHITE));
        boardInitial.put(Square.of("e8"), King.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("h8"), Rook.getInstance(Team.BLACK));
        boardInitial.put(Square.of("a1"), Rook.getInstance(Team.WHITE));
        boardInitial.put(Square.of("h1"), Rook.getInstance(Team.WHITE));
        boardInitial.put(Square.of("d1"), Knight.getInstance(Team.BLACK));
        boardInitial.put(Square.of("d8"), Knight.getInstance(Team.WHITE));
        boardInitial.put(Square.of("f1"), Knight.getInstance(Team.BLACK));
        boardInitial.put(Square.of("f8"), Knight.getInstance(Team.WHITE));
        boardInitial.put(Square.of("a2"), Pawn.getInstance(Team.WHITE));
        ChessGame chessGame = new ChessGame(ChessBoard.of(boardInitial), Team.WHITE,
            CastlingElement
                .of(new HashSet<>(Collections.singletonList(CastlingSetting.BLACK_KING_BEFORE))),
            EnPassant.createEmpty());

        assertThat(chessGame.move(new MoveInfo(whiteBefore, whiteAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
        assertThat(chessGame
            .move(new MoveInfo(Square.of("a2"), Square.of("a3"))))
            .isEqualTo(MoveState.SUCCESS);
        assertThat(chessGame.move(new MoveInfo(blackBefore, blackAfter)))
            .isEqualTo(MoveState.FAIL_CAN_NOT_MOVE);
    }

}
