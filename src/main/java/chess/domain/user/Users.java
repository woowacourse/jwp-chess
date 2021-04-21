package chess.domain.user;

import chess.domain.piece.TeamType;

import java.util.List;

public class Users {
    private static final int MAXIMUM_USER_COUNTS_FOR_ONE_ROOM = 2;

    private final List<User> users;

    public Users(List<User> users) {
        validateUsers(users);
        this.users = users;
    }

    private void validateUsers(List<User> users) {
        if (users.size() == MAXIMUM_USER_COUNTS_FOR_ONE_ROOM) {
            throw new IllegalStateException("이미 꽉 찬 방입니다.");
        }
    }

    public TeamType generateTeamType() {
        if (users.isEmpty()) {
            return TeamType.WHITE;
        }
        User existingUser = users.get(0);
        TeamType teamType = TeamType.valueOf(existingUser.getTeamType());
        return teamType.findOppositeTeam();
    }
}
