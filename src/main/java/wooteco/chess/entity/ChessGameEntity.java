package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import wooteco.chess.domain.game.Board;
import wooteco.chess.domain.game.ChessGame;
import wooteco.chess.domain.game.Turn;
import wooteco.chess.domain.game.state.Finished;
import wooteco.chess.domain.game.state.Playing;
import wooteco.chess.domain.game.state.Ready;
import wooteco.chess.domain.game.state.State;
import wooteco.chess.dto.BoardDto;

@Table("chess_game")
public class ChessGameEntity {
    private static final String PLAYING = "PLAYING";
    private static final String FINISHED = "FINISHED";

    @Id
    private int id;
    private String state;
    private String board;
    private String turn;

    public ChessGameEntity() {
    }

    public ChessGameEntity(ChessGame chessGame) {
        update(chessGame);
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }

    public void update(ChessGame chessGame) {
        this.state = chessGame.getState().toString();
        this.board = String.join("", new BoardDto(chessGame.board()).getBoard());
        this.turn = String.valueOf(chessGame.turn());
    }

    public ChessGame toModel() {
        return ChessGame.of(toStatus());
    }

    public State toStatus() {
        if (state.equals(PLAYING)) {
            return new Playing(Board.from(board), Turn.from(turn));
        }
        if (state.equals(FINISHED)) {
            return new Finished(Board.from(board), Turn.from(turn));
        }
        return new Ready();
    }
}
