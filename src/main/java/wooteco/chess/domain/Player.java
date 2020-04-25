package wooteco.chess.domain;

public class Player {
    private final String name;
    private final String password;
    private final Team team;

    public Player(String name, String password, Team team) {
        this.name = name;
        this.password = password;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Team getTeam() {
        return team;
    }
}
