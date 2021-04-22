package chess.domain.user;

import chess.domain.piece.TeamType;

import java.util.Objects;

public class User {

    private final int id;
    private final String password;
    private final String teamType;
    private final int roomId;

    public User(int id, String password, String teamType, int roomId) {
        this.id = id;
        this.password = password;
        this.teamType = teamType;
        this.roomId = roomId;
    }

    public boolean isSameTeam(TeamType teamType) {
        return this.teamType.equals(teamType.toString());
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getTeamType() {
        return teamType;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && roomId == user.roomId && password.equals(user.password) && teamType.equals(user.teamType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, teamType, roomId);
    }
}
