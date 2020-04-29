package chess.board;

import chess.exception.InvalidConstructorValueException;
import chess.location.Location;
import chess.piece.type.Piece;
import chess.team.Team;
import spring.entity.PieceEntity;

import java.util.*;
import java.util.stream.Collectors;

public class ChessBoard {
    private final Map<Location, Piece> board;

    public ChessBoard(Map<Location, Piece> board) {
        validNullValue(board);
        this.board = board;
    }

    private void validNullValue(Map<Location, Piece> board) {
        if (Objects.isNull(board) || board.containsKey(null) || board.containsValue(null)) {
            throw new InvalidConstructorValueException();
        }
    }

    public boolean canMove(Location now, Location destination) {
        Piece piece = board.get(now);
        boolean isNotSameTeam = isNotSameTeam(destination, piece);
        if (board.containsKey(now)) {
            return isNotSameTeam
                    && piece.canMove(createRoute(now, destination));
        }
        return false;
    }

    public Route createRoute(Location now, Location after) {
        Map<Location, Piece> route = new HashMap<>();

        Location next = now.calculateNextLocation(after, 1);
        route.put(now, board.get(now));
        route.put(after, board.get(after));

        while (after.equals(next) == false) {
            if (board.containsKey(next)) {
                route.put(next, board.get(next));
            }
            next = next.calculateNextLocation(after, 1);
        }

        return new Route(route, now, after);
    }


    public boolean canNotMove(Location now, Location destination) {
        return canMove(now, destination) == false;
    }

    private boolean isNotSameTeam(Location destination, Piece piece) {
        if (board.containsKey(destination)) {
            return piece.isNotSameTeam(board.get(destination));
        }
        return true;
    }

    public Map<Location, Piece> giveMyPieces(Team team) {
        return board.entrySet().stream()
                .filter(entry -> entry.getValue().isSameTeam(team))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void move(Location now, Location destination) {
        Piece piece = board.remove(now);
        board.put(destination, piece);
    }

    public boolean isNotCorrectTeam(Location location, Team team) {
        return board.get(location).isNotSame(team);
    }

    public boolean isNotExist(Location now) {
        return Objects.isNull(board.get(now));
    }

    public boolean isExistPieceIn(Location location) {
        return board.containsKey(location);
    }

    public Map<Location, Piece> getBoard() {
        return board;
    }

    public Piece getPiece(Location location) {
        return board.get(location);
    }

    public Set<PieceEntity> toEntities() {
        Set<PieceEntity> pieces = new HashSet<>();
        for (Location location : this.board.keySet()) {
            String name = String.valueOf(board.get(location).getName());
            String row = String.valueOf(location.getRowValue());
            String col = String.valueOf(location.getColValue());

            pieces.add(new PieceEntity(name, row, col));
        }
        return pieces;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Location, Piece> entry : board.entrySet()) {
            String format = String.format("locatoin : %s piece : %s \n", entry.getKey(), entry.getValue());
            sb.append(format);
        }
        return sb.toString();
    }
}
