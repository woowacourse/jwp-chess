package chess.chessgame.repository;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.User;

import java.util.List;

public interface UserRepository {
    User createUser(String password, Color color);

    List<User> findByGameId(long gameId);

    User findByUserId(long userId);

    void deleteUser(User user);
}
