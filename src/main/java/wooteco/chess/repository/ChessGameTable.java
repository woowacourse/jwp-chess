package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import wooteco.chess.domain.game.ChessGame;

import java.util.Set;
import java.util.stream.Collectors;

public class ChessGameTable {

    @Id
    private Long id;
    private Set<BoardTable> board;
    private String team;

    private ChessGameTable(Long id, Set<BoardTable> board, String team) {
        this.id = id;
        this.board = board;
        this.team = team;
    }

    private ChessGameTable(Set<BoardTable> board, String team) {
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
        return new ChessGameTable(board, chessGame.getTurn().toString());
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
