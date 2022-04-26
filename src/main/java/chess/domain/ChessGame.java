package chess.domain;

import chess.domain.piece.Piece;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.File;
import chess.domain.square.Square;
import java.util.List;
import java.util.Map;

public class ChessGame {

    private final Long id;
    private final Board board;
    private Team turn;
    private final Room room;

    public ChessGame(final Long id, final Board board, final Team turn, final Room room) {
        this.id = id;
        this.board = board;
        this.turn = turn;
        this.room = room;
    }

    public ChessGame(final Long id, final Board board, final Team turn) {
        this(id, board, turn, null);
    }

    public ChessGame(final Board board, final Team turn, final Room room) {
        this(null, board, turn, room);
    }

    public static ChessGame initGame() {
        return new ChessGame(null, new Board(BoardInitializer.create()), Team.WHITE);
    }

    public void move(final Square from, final Square to) {
        validateTurn(from);
        board.move(from, to);
        this.turn = turn.reverse();
        validateEnd();
    }

    public Result createResult() {
        if (board.isKingDead()) {
            return new Result(calculateByTeam(Team.BLACK), calculateByTeam(Team.WHITE), board.getTeamWithAliveKing());
        }
        return Result.byScore(calculateByTeam(Team.BLACK), calculateByTeam(Team.WHITE));
    }

    public void terminate() {
        turn = Team.NONE;
    }

    public boolean isEnd() {
        return turn == Team.NONE;
    }

    private void validateTurn(final Square from) {
        if (turn == Team.NONE) {
            throw new IllegalStateException("이미 종료된 게임입니다.");
        }

        if (!board.isSameTeam(from, turn)) {
            throw new IllegalArgumentException(
                    String.format("현재는 %s의 차례입니다. 차례에 맞는 기물을 움직여주세요.", turn.name())
            );
        }
    }

    private void validateEnd() {
        if (board.isKingDead()) {
            turn = Team.NONE;
        }
    }

    private Double calculateByTeam(final Team team) {
        Map<File, List<Piece>> files = board.findFilesByTeam(team);
        double total = 0;
        for (File file : files.keySet()) {
            total += getTotal(files, file);
        }
        return total;
    }

    private double getTotal(final Map<File, List<Piece>> files, final File file) {
        final List<Piece> pieces = files.get(file);
        final int pawnCount = pawnCountByFile(pieces);
        final double total = pieces.stream()
                .mapToDouble(Piece::getScore)
                .sum();

        return total - pawnPenaltyScore(pawnCount);
    }

    private double pawnPenaltyScore(final int pawnCount) {
        if (pawnCount >= 2) {
            return pawnCount * 0.5;
        }
        return 0;
    }

    private int pawnCountByFile(final List<Piece> pieces) {
        return (int) pieces.stream()
                .map(Piece::getPieceType)
                .filter(pieceType -> pieceType == PieceType.PAWN)
                .count();
    }

    public Long getId() {
        return id;
    }

    public Board getBoard() {
        return board;
    }

    public Team getTurn() {
        return turn;
    }

    public Participant getParticipant() {
        return room.getParticipant();
    }

    public Long getWhiteId() {
        return getParticipant().getWhiteId();
    }

    public Long getBlackId() {
        return getParticipant().getBlackId();
    }

    public Long getWinnerId() {
        final Result result = createResult();
        final Team team = result.getWinner();
        return getParticipant().getIdByTeam(team);
    }

    public Room getRoomInfo() {
        return room;
    }

    public String getTitle() {
        return room.getTitle();
    }

    public String getPassword() {
        return room.getPassword();
    }
}
