package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.board.MoveFailureException;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {

    private final long id;
    private final String name;
    private final long hostId;
    private final long guestId;
    private final LocalDateTime createdTime;
    private final Board board;
    private Team turn;
    private boolean isFinished;

    public Game(final long id, final String name, final Team turn, final long hostId,
        final long guestId, final boolean isFinished, final LocalDateTime createdTime,
        final Board board) {

        this.id = id;
        this.name = name;
        this.turn = turn;
        this.hostId = hostId;
        this.guestId = guestId;
        this.isFinished = isFinished;
        this.createdTime = createdTime;
        this.board = board;
    }

    public static Game of(final long id, final String name, final Team turn,
        final long hostId, final long guestId, final boolean finished,
        final LocalDateTime createdTime, final Board board) {

        return new Game(id, name, turn, hostId, guestId, finished, createdTime, board);
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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTurn() {
        return turn;
    }

    public long getHostId() {
        return hostId;
    }

    public long getGuestId() {
        return guestId;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public Board getBoard() {
        return board;
    }
}
