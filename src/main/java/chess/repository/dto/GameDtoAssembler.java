package chess.repository.dto;

import java.util.List;

import chess.domain.Color;
import chess.domain.game.Game;
import chess.domain.game.state.GameStateFactory;
import chess.domain.player.Player;
import chess.domain.player.Players;
import chess.repository.dto.game.GameDto;
import chess.repository.dto.game.GameUpdateDto;

public class GameDtoAssembler {

    public GameDtoAssembler() {
    }

    public static Game toChessGame(final Long gameId, final List<Player> players, final GameDto gameDto) {
        return Game.loadGame(gameId, gameDto.getTitle(), gameDto.getPassword(), GameStateFactory.loadGameState(
                new Players(players), gameDto.getFinished(), Color.from(gameDto.getCurrentTurnColor())));
    }

    public static GameDto toGameDto(final Game game, final List<Player> players) {
        final Color currentTurnColor = game.getColorOfCurrentTurn();
        final Boolean finished = game.isFinished();
        return new GameDto(0L, game.getTitle(), game.getPassword(),
                players.get(0).getId(), players.get(1).getId(),
                finished, currentTurnColor.getName());
    }

    public static GameUpdateDto toGameUpdateDto(final Game game) {
        final Long gameId = game.getId();
        final boolean finished = game.isFinished();
        final Color currentTurnColor = game.getColorOfCurrentTurn();
        return new GameUpdateDto(gameId, finished, currentTurnColor.getName());
    }
}
