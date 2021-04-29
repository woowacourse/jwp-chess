package chess.service;

import chess.domain.piece.Owner;
import chess.service.dao.PlayerDao;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private static final int MAXIMUM_SIZE_OF_ROOM = 2;

    private final RoomDao roomDao;
    private final PlayerDao playerDao;

    public PlayerService(final RoomDao roomDao, final PlayerDao playerDao) {
        this.roomDao = roomDao;
        this.playerDao = playerDao;
    }

    public void enter(final long roomId, final String playerId) {
        final List<String> players = playerDao.players(roomId);

        if (isAlreadyJoined(players, playerId)) {
            return;
        }

        if (isFull(players)) {
            throw new IllegalArgumentException("이미 가득 찬 방입니다.");
        }

        playerDao.enter(roomId, playerId);
    }

    private boolean isAlreadyJoined(final List<String> players, final String playerId) {
        return players.contains(playerId);
    }

    private boolean isFull(final List<String> players) {
        return players.size() == MAXIMUM_SIZE_OF_ROOM;
    }

    public Owner ownerOfPlayer(final long roomId, final String playerId) {
        return playerDao.getOwner(roomId, playerId);
    }
}
