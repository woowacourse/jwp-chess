package wooteco.chess.repository;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.PieceType;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public class ChessGameTable {

    @Id
    private Long id;
    private String title;
    private Set<BoardTable> board;
    private String turn;

    private ChessGameTable() {
    }

    private ChessGameTable(Long id, String title, Set<BoardTable> board, String turn) {
        this.id = id;
        this.title = title;
        this.board = board;
        this.turn = turn;
    }

    public static ChessGameTable createForInsert(ChessGame chessGame) {
        Set<BoardTable> board = toBoardTable(chessGame.getBoard());
        return new ChessGameTable(null, chessGame.getTitle(), board, chessGame.getTurn().toString());
    }

    public static ChessGameTable createForUpdate(ChessGame chessGame) {
        Set<BoardTable> board = toBoardTable(chessGame.getBoard());
        return new ChessGameTable(chessGame.getId(), chessGame.getTitle(), board, chessGame.getTurn().toString());
    }

    private static Set<BoardTable> toBoardTable(Map<Position, PieceState> board) {
        return board.entrySet()
            .stream()
            .map(entry ->
                new BoardTable(
                    entry.getKey().getName(),
                    entry.getValue().getPieceType().toString(),
                    entry.getValue().getTeam().toString()
                ))
            .collect(Collectors.toSet());
    }

    public ChessGame toChessGame() {
        Map<Position, PieceState> board = this.board.stream()
            .collect(Collectors.toMap(
                table -> Position.of(table.getPosition()),
                table -> createPieceState(table.getPiece(), table.getPosition(), table.getTeam()))
            );

        return ChessGame.of(id, title, Board.of(board), Turn.from(Team.valueOf(turn)));
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
        return turn;
    }

    public String getTitle() {
        return title;
    }
}
