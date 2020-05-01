package wooteco.chess.repository;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class ChessGameTable {

    @Id
    private Long id;
    private Set<BoardTable> board;
    private String team;

    private ChessGameTable() {
    }

    private ChessGameTable(Long id, Set<BoardTable> board, String team) {
        this.id = id;
        this.board = board;
        this.team = team;
    }

    public static ChessGameTable createForSave(ChessGame chessGame) {
        Set<BoardTable> board = chessGame.getBoard().entrySet()
            .stream()
            .map(entry ->
                new BoardTable(
                    entry.getKey().getName(),
                    entry.getValue().getPieceType().toString(),
                    entry.getValue().getTeam().toString()
                ))
            .collect(Collectors.toSet());
        return new ChessGameTable(null, board, chessGame.getTurn().toString());
    }

    public static ChessGameTable createForUpdate(ChessGame chessGame) {
        Set<BoardTable> board = chessGame.getBoard().entrySet()
            .stream()
            .map(entry ->
                new BoardTable(
                    entry.getKey().getName(),
                    entry.getValue().getPieceType().toString(),
                    entry.getValue().getTeam().toString()
                ))
            .collect(Collectors.toSet());
        return new ChessGameTable(chessGame.getId(), board, chessGame.getTurn().toString());
    }

    public ChessGame toChessGame() {
        Map<Position, PieceState> board = this.board.stream()
            .collect(Collectors.toMap(
                table -> Position.of(table.getPosition()),
                table -> createPieceState(table.getPiece(), table.getPosition(), table.getTeam()))
            );

        return ChessGame.of(id, Board.of(board), Team.valueOf(team));
    }

    private PieceState createPieceState(final String piece, final String position, final String team) {
        PieceType type = PieceType.valueOf(piece);
        return type.apply(Position.of(position), Team.valueOf(team));
    }

    public Long getId() {
        return id;
    }

    public Set<BoardTable> getBoard() {
        return board;
    }

    public String getTeam() {
        return team;
    }
}
