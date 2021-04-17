//package chess.spring.domain;
//
//import chess.web.domain.piece.type.Direction;
//import chess.web.domain.position.Position;
//
//public class Move {
//
//    private final Position startPosition;
//    private final Position destination;
//
//    public Move(Position startPosition, Position destination) {
//        this.startPosition = startPosition;
//        this.destination = destination;
//    }
//
//    public Position getStartPosition() {
//        return startPosition;
//    }
//
//    public Position getDestination() {
//        return destination;
//    }
//
//    public Direction getDirection() {
//        return startPosition.calculateDirection(destination);
//    }
//
//    public Position getNextPositionOfStartPosition() {
//        return startPosition.moveTo(getDirection());
//    }
//
//    public boolean isDestination(Position position) {
//        return position.equals(destination);
//    }
//
//    public boolean isRankForwardedBy(int rankDiff) {
//        return startPosition.isRankForwardedBy(destination, rankDiff);
//    }
//
//    public boolean isStartPositionFirstPawnPosition() {
//        return startPosition.isFirstPawnPosition(currentTurnTeamColor);
//    }
//
//    public Long getDestinationId() {
//        return destination.getId();
//    }
//}
