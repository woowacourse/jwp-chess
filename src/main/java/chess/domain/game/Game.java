package chess.domain.game;

import chess.domain.game.board.Board;
import chess.domain.game.board.MoveFailureException;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.board.piece.location.Location;
import chess.domain.game.room.Room;
import chess.domain.game.team.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {

    private final long id;
    private final LocalDateTime createdTime;
    private final Board board;
    private final Room room;
    private Team turn;
    private boolean isFinished;

    private Game(final long id, final LocalDateTime createdTime, final Team turn,
        final boolean isFinished, final Board board, final Room room) {

        this.id = id;
        this.createdTime = createdTime;
        this.turn = turn;
        this.isFinished = isFinished;
        this.board = board;
        this.room = room;
    }

    public static Game of(final long id, final LocalDateTime createdTime, final Team turn,
        final boolean isFinished, final Board board, final Room room) {

        return new Game(id, createdTime, turn, isFinished, board, room);
    }

    public boolean checkMovement(final String source, final String target, final Team turn) {
        if (!this.turn.equals(turn)) {
            return false;
        }
        return board.isMovable(Location.convert(source), Location.convert(target), turn);
    }

    public void move(final String source, final String target, final Team turn) {
        validateGameOver();
        board.move(Location.convert(source), Location.convert(target), turn);
        if (board.isKingCatch()) {
            this.isFinished = true;
            return;
        }
        this.turn = turn.reverse();
    }

    private void validateGameOver() {
        if (isFinished) {
            throw new MoveFailureException("게임이 끝나서 움직일 수 없습니다.");
        }
    }

    public List<Piece> bringMovedPieces(final Game other) {
        final Map<Long, Location> locations = toPieces().stream()
            .collect(Collectors.toMap(Piece::getId, Piece::getLocation));

        return other.toPieces()
            .stream()
            .filter(otherPiece -> locations.containsKey(otherPiece.getId()))
            .filter(
                otherPiece -> !otherPiece.getLocation().equals(locations.get(otherPiece.getId()))
            )
            .collect(Collectors.toList());
    }

    public List<Long> toPieceIds() {
        return toPieces().stream()
            .map(Piece::getId)
            .collect(Collectors.toList());
    }

    public List<Piece> toPieces() {
        return board.toList();
    }

    public String getRoomName() {
        return room.getName();
    }

    public long getHostId() {
        return room.getHostId();
    }

    public Long getGuestId() {
        return room.getGuestId();
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public Team getTurn() {
        return turn;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Board getBoard() {
        return board;
    }

    public Room getRoom() {
        return room;
    }

}
