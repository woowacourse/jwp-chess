package chess.repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import chess.domain.game.Game;
import chess.domain.player.Player;
import chess.domain.player.Players;
import chess.repository.dto.GameDtoAssembler;
import chess.repository.dto.PlayerDtoAssembler;
import chess.repository.dto.game.GameDto;
import chess.repository.dto.game.GameFinishedDto;
import chess.repository.dto.player.PlayerDto;

@Repository
public class ChessRepository {

    private final GameDao gameDao;
    private final PlayerDao playerDao;

    public ChessRepository(final GameDao gameDao, final PlayerDao playerDao) {
        this.gameDao = gameDao;
        this.playerDao = playerDao;
    }

    public Game save(final Game game) {
        return saveGame(game, savePlayers(game.getPlayers()));
    }

    private List<Player> savePlayers(final Players players) {
        return players.getPlayers()
                .stream()
                .map(this::savePlayer)
                .collect(Collectors.toUnmodifiableList());
    }

    private Player savePlayer(final Player player) {
        final PlayerDto playerDto = PlayerDtoAssembler.toPlayerDto(player);
        return PlayerDtoAssembler.toPlayer(playerDao.save(playerDto), playerDto);
    }

    private Game saveGame(final Game game, final List<Player> players) {
        final GameDto gameDto = GameDtoAssembler.toGameDto(game, players);
        return GameDtoAssembler.toChessGame(gameDao.save(gameDto), players, gameDto);
    }

    public Game findById(final Long gameId) {
        final GameDto gameDto = gameDao.findById(gameId);
        final List<Player> players = List.of(
                findPlayerById(gameDto.getPlayer_id1()),
                findPlayerById(gameDto.getPlayer_id2()));
        return GameDtoAssembler.toChessGame(gameId, players, gameDto);
    }

    public Player findPlayerById(final Long playerId) {
        final PlayerDto playerDto = playerDao.findById(playerId);
        return PlayerDtoAssembler.toPlayer(playerDto);
    }

    public Map<Long, Boolean> findStatuses() {
        final Map<Long, Boolean> statuses = new LinkedHashMap<>();
        for (final GameFinishedDto gameFinishedDto : gameDao.findIdAndFinished()) {
            statuses.put(gameFinishedDto.getId(), gameFinishedDto.getFinished());
        }
        return Collections.unmodifiableMap(statuses);
    }

    public Game update(final Game game) {
        updatePlayers(game.getPlayers());
        updateGame(game);
        return findById(game.getId());
    }

    private void updatePlayers(final Players players) {
        for (final Player player : players.getPlayers()) {
            playerDao.update(PlayerDtoAssembler.toPlayerDto(player));
        }
    }

    private void updateGame(final Game game) {
        gameDao.update(GameDtoAssembler.toGameUpdateDto(game));
    }

    public void remove(final Long gameId) {
        removePlayers(gameId);
        removeGame(gameId);
    }

    private void removePlayers(final Long gameId) {
        final Players players = findById(gameId).getPlayers();
        for (final Player player : players.getPlayers()) {
            playerDao.remove(player.getId());
        }
    }

    private void removeGame(final Long gameId) {
        gameDao.remove(gameId);
    }
}
