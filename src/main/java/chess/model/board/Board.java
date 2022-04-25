package chess.model.board;

import static java.util.stream.Collectors.toMap;

import chess.model.game.GameResult;
import chess.model.piece.Empty;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.model.strategy.move.Direction;
import chess.model.strategy.move.MoveType;
import chess.service.dto.BoardDto;
import java.util.Map;

public final class Board {

    private final Map<Square, Piece> board;

    private Board(Map<Square, Piece> board) {
        this.board = board;
    }

    public Board(BoardInitializer boardInitializer) {
        this(boardInitializer.initPieces());
    }

    public Board(BoardDto boardDto) {
        this(boardDto.getPieces()
                .stream()
                .collect(toMap(dto -> Square.of(dto.getSquare()), PieceType::createPiece)));
    }

    public static Board init() {
        return new Board(new ChessInitializer());
    }

    public Piece findPieceBySquare(Square square) {
        if (board.containsKey(square)) {
            return board.get(square);
        }
        throw new IllegalArgumentException("해당 위치의 값을 찾을 수 없습니다.");
    }

    public MoveResult move(Square from, Square to) {
        Piece movePiece = findPieceBySquare(from);
        Piece targetPiece = findPieceBySquare(to);
        checkCanMove(from, to, movePiece, targetPiece);
        checkHasPieceInRoute(from, to);
        updateBoard(from, to, movePiece);
        return MoveResult.from(from, to,
                findPieceBySquare(from), findPieceBySquare(to), targetPiece);
    }

    private void checkCanMove(Square from, Square to, Piece movePiece, Piece targetPiece) {
        if (!movePiece.movable(from, to, getMoveType(movePiece, targetPiece))) {
            throw new IllegalArgumentException("해당 칸으로 이동할 수 없습니다.");
        }
    }

    private MoveType getMoveType(Piece sourcePiece, Piece targetPiece) {
        if (targetPiece.isAlly(sourcePiece)) {
            throw new IllegalArgumentException("동료를 공격할 수 없습니다.");
        }
        return MoveType.of(sourcePiece.isEnemy(targetPiece));
    }

    private void checkHasPieceInRoute(Square sourceSquare, Square targetSquare) {
        Square tempSquare = sourceSquare;
        Direction direction = sourceSquare.findDirection(targetSquare);
        while (tempSquare.isDifferent(targetSquare)) {
            tempSquare = tempSquare.move(direction);
            checkHasPieceInSquare(targetSquare, tempSquare);
        }
    }

    private void checkHasPieceInSquare(Square targetSquare, Square tempSquare) {
        if (findPieceBySquare(tempSquare).isPlayerPiece() && tempSquare.isDifferent(targetSquare)) {
            throw new IllegalArgumentException("경로 중 기물이 존재하여 이동할 수 없습니다.");
        }
    }

    private void updateBoard(Square sourceSquare, Square targetSquare, Piece sourcePiece) {
        board.replace(targetSquare, sourcePiece);
        board.replace(sourceSquare, new Empty());
    }

    public Score calculateScore() {
        return Score.of(board);
    }

    public GameResult getResult() {
        return GameResult.from(board, calculateScore());
    }

    public Map<Square, Piece> getBoard() {
        return board;
    }
}
