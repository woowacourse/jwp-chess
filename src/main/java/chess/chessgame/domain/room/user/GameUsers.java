package chess.chessgame.domain.room.user;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;

import java.util.List;

public class GameUsers {
    public static final int MAX_GAME_USER_SIZE = 2;

    private final List<User> users;

    public GameUsers(List<User> users) {
        if (users.size() > MAX_GAME_USER_SIZE) {
            throw new IllegalArgumentException(String.format("유저 수는 최대 %d명 입니다.", MAX_GAME_USER_SIZE));
        }
        this.users = users;
    }

    public void enterUser(User user) {
        if (users.size() + 1 > MAX_GAME_USER_SIZE) {
            throw new IllegalStateException(String.format("유저 수는 최대 %d명 입니다.", MAX_GAME_USER_SIZE));
        }
        users.add(user);
    }

    public User findUserByColor(Color color) {
        return users.stream()
                .filter(user -> user.isSameColor(color))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("%s 색상의 유저가 없습니다.", color.name())));
    }
}
