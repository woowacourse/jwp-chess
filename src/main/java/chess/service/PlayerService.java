package chess.service;

import chess.domain.piece.Owner;
import chess.dto.RoomDto;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private static final int MAXIMUM_SIZE_OF_ROOM = 2;

    private final RoomDao roomDao;

    public PlayerService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public void enter(final long roomId, final String playerId) {
        final List<String> players = roomDao.players(roomId);

        if (isAlreadyJoined(players, playerId)) {
            return;
        }

        if (isFull(players)) {
            throw new IllegalArgumentException("이미 가득 찬 방입니다.");
        }

        roomDao.enter(roomId, playerId);
    }

    private boolean isAlreadyJoined(final List<String> players, final String playerId) {
        return players.contains(playerId);
    }

    private boolean isFull(final List<String> players) {
        return players.size() == MAXIMUM_SIZE_OF_ROOM;
    }

    public Owner ownerOfPlayer(final long roomId, final String playerId) {
        final RoomDto roomInfo = roomDao.roomInfo(roomId);

        if (isPlayer1(roomInfo, playerId)) {
            return Owner.WHITE;
        }

        if (isPlayer2(roomInfo, playerId)) {
            return Owner.BLACK;
        }

        throw new IllegalArgumentException("적절하지 않은 사용자입니다.");
    }

    private boolean isPlayer1(final RoomDto roomInfo, final String playerId) {
        final String player1 = roomInfo.getPlayer1();
        return player1.equals(playerId);
    }

    private boolean isPlayer2(final RoomDto roomInfo, final String playerId) {
        final String player2 = roomInfo.getPlayer2();
        return player2.equals(playerId);
    }
}
