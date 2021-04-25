package chess.service;

import chess.controller.dto.RoomDto;
import chess.domain.piece.Owner;
import chess.service.dao.RoomDao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlayerService {
    private static final int MAXIMUM_SIZE_OF_ROOM = 2;

    private final RoomDao roomDao;

    public PlayerService(final RoomDao roomDao){
        this.roomDao = roomDao;
    }

    public void enter(final long roomId, final String playerId) {
        final List<String> players = roomDao.players(roomId);
        if (players.contains(playerId)) {
            return;
        }
        if (players.size() == MAXIMUM_SIZE_OF_ROOM) {
            throw new IllegalArgumentException("이미 가득 찬 방입니다.");
        }
        roomDao.enter(roomId, playerId);
    }

    public Owner ownerOfPlayer(final Long roomId, final String playerId) {
        final RoomDto roomInfo = roomDao.roomInfo(roomId);
        final String player1 = roomInfo.getPlayer1();
        final String player2 = roomInfo.getPlayer2();

        if (player1.equals(playerId)) {
            return Owner.WHITE;
        }

        if (player2.equals(playerId)) {
            return Owner.BLACK;
        }

        throw new IllegalArgumentException("적절하지 않은 사용자입니다.");
    }
}
