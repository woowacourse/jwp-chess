package chess.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import chess.domain.ChessRepository;
import chess.domain.game.Game;
import chess.domain.player.Player;
import chess.domain.player.Players;
import chess.repository.dao.GameDao;
import chess.repository.dao.PlayerDao;
import chess.repository.dao.dto.DaoDtoAssembler;
import chess.repository.dao.dto.game.GameDto;
import chess.repository.dao.dto.player.PlayerDto;
import chess.repository.dto.RepositoryDtoAssembler;
import chess.repository.dto.game.GameStatus;

@Repository
public class ChessRepositoryImpl implements ChessRepository {

    private final GameDao gameDao;
    private final PlayerDao playerDao;

    public ChessRepositoryImpl(final GameDao gameDao, final PlayerDao playerDao) {
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
        final PlayerDto playerDto = DaoDtoAssembler.playerDto(player);
        return DaoDtoAssembler.player(playerDao.save(playerDto), playerDto);
    }

    private Game saveGame(final Game game, final List<Player> players) {
        final GameDto gameDto = DaoDtoAssembler.gameDto(game, players);
        return DaoDtoAssembler.game(gameDao.save(gameDto), players, gameDto);
    }

    public Game findById(final Long gameId) {
        final GameDto gameDto = gameDao.findById(gameId);
        final List<Player> players = List.of(
                findPlayerById(gameDto.getPlayer_id1()),
                findPlayerById(gameDto.getPlayer_id2()));
        return DaoDtoAssembler.game(gameId, players, gameDto);
    }

    private Player findPlayerById(final Long playerId) {
        final PlayerDto playerDto = playerDao.findById(playerId);
        return DaoDtoAssembler.player(playerDto);
    }

    public List<GameStatus> findStatuses() {
        return gameDao.findStatuses()
                .stream()
                .map(RepositoryDtoAssembler::gameStatus)
                .collect(Collectors.toUnmodifiableList());
    }

    public Game update(final Game game) {
        updatePlayers(game.getPlayers());
        updateGame(game);
        return findById(game.getId());
    }

    private void updatePlayers(final Players players) {
        for (final Player player : players.getPlayers()) {
            playerDao.update(DaoDtoAssembler.playerDto(player));
        }
    }

    private void updateGame(final Game game) {
        gameDao.update(DaoDtoAssembler.gameUpdateDto(game));
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
