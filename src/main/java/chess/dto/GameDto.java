package chess.dto;

import chess.domain.game.Game;
import chess.dto.board.RowDto;
import chess.dto.board.WebBoardViewDto;
import java.util.List;
import java.util.Objects;

public class GameDto {

    private final GameDataDto game;
    private final WebBoardViewDto board;

    public GameDto(GameDataDto game, WebBoardViewDto board) {
        this.game = game;
        this.board = board;
    }

    public static GameDto of(int gameId, Game game) {
        return new GameDto(new GameDataDto(gameId, game.getState()), new WebBoardViewDto(game.getBoard()));
    }

    public GameDataDto getGame() {
        return game;
    }

    public List<RowDto> getBoard() {
        return board.toDisplay();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameDto gameDto = (GameDto) o;
        return Objects.equals(game, gameDto.game)
                && Objects.equals(board, gameDto.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, board);
    }

    @Override
    public String toString() {
        return "GameDto{" + "game=" + game + ", board=" + board + '}';
    }
}
