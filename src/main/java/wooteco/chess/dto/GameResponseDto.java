package wooteco.chess.dto;

import java.util.Map;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Side;
import wooteco.chess.domain.player.Player;

public class GameResponseDto {
    private final String title;
    private final Map<Side, Player> players;
    private final Map<Position, Piece> board;

    public GameResponseDto(final Game game) {
        this.title = game.getTitle();
        this.players = game.getPlayers();
        this.board = game.getBoard().getBoard();
    }

    public String getTitle() {
        return title;
    }

    public Map<Side, Player> getPlayers() {
        return players;
    }

    public Map<Position, Piece> getBoard() {
        return board;
    }
}
