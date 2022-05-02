package chess.domain.board;

import chess.domain.board.piece.Empty;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Pieces;
import chess.domain.board.piece.position.Position;

import java.util.List;

public final class Board {

    private final Pieces pieces;
    private final Turn turn;

    private Board(Pieces pieces, Turn turn) {
        this.pieces = pieces;
        this.turn = turn;
    }

    public static Board create(Pieces pieces, Turn turn) {
        return new Board(pieces, turn);
    }

    public Pieces getPieces() {
        return pieces;
    }

    public Turn getTurn() {
        return turn;
    }

    public void move(List<String> commandPosition, Turn thisTurn) {
        Position sourcePosition = getSource(commandPosition);
        Position targetPosition = getTarget(commandPosition);

        Piece sourcePiece = pieces.findByPosition(sourcePosition);
        Piece targetPiece = pieces.findByPosition(targetPosition);
        validateTurn(thisTurn, sourcePiece);
        validateMovable(targetPosition, sourcePiece, targetPiece);
        move(sourcePosition, targetPosition, sourcePiece, targetPiece);
    }

    private void validateTurn(Turn thisTurn, Piece sourcePiece) {
        if (sourcePiece.isNotCurrentTurn(thisTurn)) {
            System.out.println("말의 색 >> " + sourcePiece.getTeam().value());
            System.out.println("현재 턴 >> " + thisTurn.getTeam().value());
            throw new IllegalArgumentException("[ERROR] 현재 차례가 아닙니다.");
        }
    }

    private void validateMovable(Position targetPosition, Piece sourcePiece, Piece targetPiece) {
        if (isNotMovable(targetPosition, sourcePiece, targetPiece)) {
            throw new IllegalArgumentException("[ERROR] 움직일수 없습니다.");
        }
    }

    private boolean isNotMovable(Position targetPosition, Piece sourcePiece, Piece targetPiece) {
        return (!sourcePiece.isMovableRange(targetPosition) || hasBlock(sourcePiece, targetPiece)) &&
                !sourcePiece.isKill(targetPiece);
    }

    private void move(Position sourcePosition, Position targetPosition, Piece sourcePiece, Piece targetPiece) {
        sourcePiece.moveTo(targetPosition);
        pieces.remove(targetPiece);
        pieces.add(new Empty(sourcePosition));
    }

    private boolean hasBlock(Piece sourcePiece, Piece targetPiece) {
        if (sourcePiece.isSameTeam(targetPiece)) {
            return true;
        }
        List<Position> positions = sourcePiece.getIntervalPosition(targetPiece);
        return positions.stream()
                .anyMatch(position -> !pieces.findByPosition(position).equals(new Empty(position)));
    }

    private Position getSource(List<String> commandPosition) {
        return Position.of(
                commandPosition.get(0).charAt(0),
                commandPosition.get(0).charAt(1));
    }

    private Position getTarget(List<String> commandPosition) {
        return Position.of(
                commandPosition.get(1).charAt(0),
                commandPosition.get(1).charAt(1)
        );
    }

    public boolean isDeadKing() {
        return pieces.countOfKing() == 1;
    }

    public Board updateTurn(Turn turn) {
        return Board.create(pieces, turn);
    }

    public Piece findPieceByPosition(Position position) {
        return pieces.findByPosition(position);
    }
}

