//package chess.spring.domain;
//
//import static chess.web.domain.piece.type.PieceType.BISHOP;
//import static chess.web.domain.piece.type.PieceType.KING;
//import static chess.web.domain.piece.type.PieceType.KNIGHT;
//import static chess.web.domain.piece.type.PieceType.QUEEN;
//import static chess.web.domain.piece.type.PieceType.ROOK;
//
//import chess.web.domain.board.Cell;
//import chess.web.domain.piece.Piece;
//import chess.web.domain.piece.type.Direction;
//import chess.web.domain.piece.type.PieceType;
//import chess.web.domain.position.Position;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//public class MoveCheckerNew {
//
//    private static final String CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE = "이동할 수 없는 도착위치 입니다.";
//
//    public void validateMove(Move move, Map<Position, Cell> cells) {
//        PieceType pieceTypeToMove = getPieceTypeToMove(move, cells);
//        if (isPieceTypeMoveByDefaultStrategy(pieceTypeToMove)) {
//            validateDefaultMoving(move, cells);
//            return;
//        }
//        if (isPieceTypeMoveBySpecialStrategy(pieceTypeToMove)) {
//            validateMovingByKnightMoveStrategy(move, cells);
//            return;
//        }
//        validateMovingByPawnMoveStrategy(move, cells);
//    }
//
//    private PieceType getPieceTypeToMove(Move move, Map<Position, Cell> cells) {
//        Piece piece = getStartPositionPiece(cells, move);
//        return piece.getPieceType();
//    }
//
//    private Piece getStartPositionPiece(Map<Position, Cell> cells, Move move) {
//        Cell startPositionCell = cells.get(move.getStartPosition());
//        return startPositionCell.getPiece();
//    }
//
//    private boolean isPieceTypeMoveByDefaultStrategy(PieceType pieceTypeToMove) {
//        return pieceTypeToMove == ROOK
//            || pieceTypeToMove == BISHOP
//            || pieceTypeToMove == QUEEN;
//    }
//
//    private boolean isPieceTypeMoveBySpecialStrategy(PieceType pieceTypeToMove) {
//        return pieceTypeToMove == KNIGHT || pieceTypeToMove == KING;
//    }
//
//    private void validateDefaultMoving(Move move, Map<Position, Cell> cells) {
//        Piece startPositionPieceToMove = getStartPositionPiece(cells, move);
//        if (isNotCorrectDirection(move, startPositionPieceToMove)
//            || isAnyPieceExistsOnRouteBeforeDestination(cells, move)) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//        if (isOwnPieceExistsInDestination(cells, move)) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//    }
//
//    private void validateMovingByPawnMoveStrategy(Move move, Map<Position, Cell> cells) {
//        Piece startPositionPieceToMove = getStartPositionPiece(cells, move);
//        if (isNotCorrectDirection(move, startPositionPieceToMove)) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//        Direction moveRequestDirection = move.getDirection();
//        if (moveRequestDirection.isForward()) {
//            validateMovingForward(move, cells);
//            return;
//        }
//        validateMovingDiagonal(move, cells);
//    }
//
//    private void validateMovingForward(Move move, Map<Position, Cell> cells) {
//        if (move.isRankForwardedBy(1)) {
//            validateMovingOneRankForward(move, cells);
//            return;
//        }
//        if (move.isRankForwardedBy(2)) {
//            validateMovingTwoRankForward(move, cells);
//            return;
//        }
//        throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//    }
//
//    private void validateMovingOneRankForward(Move move, Map<Position, Cell> cells) {
//        if (isAnyPieceExistsAtDestination(cells, move.getDestination())) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//    }
//
//    private void validateMovingTwoRankForward(Move move, Map<Position, Cell> cells) {
//        if (!move.isStartPositionFirstPawnPosition()) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//        if (isAnyPieceExistsOnRouteBeforeDestination(cells, move)
//            || isAnyPieceExistsAtDestination(cells, move.getDestination())) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//    }
//
//    private void validateMovingDiagonal(Move move, Map<Position, Cell> cells) {
//        Position nextPosition = move.getNextPositionOfStartPosition();
//        if (!(isEnemyExistsAtDestination(cells, move)
//            && nextPosition.equals(move.getDestination()))) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//    }
//
//    private void validateMovingByKnightMoveStrategy(Move move, Map<Position, Cell> cells) {
//        Piece startPositionKnightToMove = getStartPositionPiece(cells, move);
//        if (isNotCorrectDirection(move, startPositionKnightToMove)) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//        Position nextPosition = move.getNextPositionOfStartPosition();
//        if (!(move.isDestination(nextPosition)
//            && !isOwnPieceExistsInDestination(cells, move))) {
//            throw new IllegalArgumentException(CANNOT_MOVE_TO_DESTINATION_ERROR_MESSAGE);
//        }
//    }
//
//    private boolean isNotCorrectDirection(Move move, Piece startPositionPieceToMove) {
//        return !startPositionPieceToMove.isCorrectMoveDirection(move.getDirection());
//    }
//
//    public boolean isOwnPieceExistsInDestination(Map<Position, Cell> cells, Move move) {
//        Cell destinationCell = findCell(cells, move.getDestination());
//        if (destinationCell.isEmpty()) {
//            return false;
//        }
//        return destinationCell.getTeamColor() == move.getCurrentTurnTeamColor();
//    }
//
//    private Cell findCell(Map<Position, Cell> cells, Position position) {
//        return cells.get(position);
//    }
//
//    public boolean isAnyPieceExistsOnRouteBeforeDestination(Map<Position, Cell> cells, Move move) {
//        Position movingPosition = move.getNextPositionOfStartPosition();
//        List<Position> canMovePositions = new ArrayList<>();
//        while (!movingPosition.equals(move.getDestination())) {
//            canMovePositions.add(movingPosition);
//            movingPosition = movingPosition.moveTo(move.getDirection());
//        }
//        return !canMovePositions.stream()
//            .map(cells::get)
//            .allMatch(Cell::isEmpty);
//    }
//
//    public boolean isAnyPieceExistsAtDestination(Map<Position, Cell> cells, Position destination) {
//        Cell destinationCell = findCell(cells, destination);
//        return !destinationCell.isEmpty();
//    }
//
//    public boolean isEnemyExistsAtDestination(Map<Position, Cell> cells, Move move) {
//        Cell destinationCell = findCell(cells, move.getDestination());
//        return !destinationCell.isEmpty()
//            && destinationCell.getTeamColor() != move.getCurrentTurnTeamColor();
//    }
//}
