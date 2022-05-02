package chess.entity;

import chess.domain.Team;

public class Room {
    private final Long id;
    private final Team team;
    private final String title;
    private final String password;
    private boolean status;

    public Room(Long id, Team team, String title, String password, boolean status) {
        this.id = id;
        this.team = team;
        this.title = title;
        this.password = password;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public boolean getStatus() {
        return status;
    }

    public void validateCanDelete(String password) {
        if (status) {
            throw new IllegalArgumentException("진행 중인 게임은 삭제할 수 없습니다.");
        }
        if (!this.password.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
        }
    }
}
