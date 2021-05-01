package chess.chessgame.repository;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.User;

public interface UserRepository {
    User createUser(Color color, String password);

    User matchPasswordUser(long roomId, String password);

    User updateRoomId(User user, long roomId);

    User findByUserId(long userId);

    void deleteUser(User user);
}
