package chess.entity;

public class ChessGameEntity {

    private final long id;
    private final String name;
    private final String password;
    private final boolean power;
    private final String teamValueOfTurn;

    public ChessGameEntity(final long id, final String name, final String password, final boolean power,
                           final String teamValueOfTurn) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.power = power;
        this.teamValueOfTurn = teamValueOfTurn;
    }

    public void isPower() {
        if (power) {
            throw new IllegalStateException("[ERROR] 아직 게임이 끝나지 않아 삭제할 수 없습니다!");
        }
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean getPower() {
        return power;
    }

    public String getTeamValueOfTurn() {
        return teamValueOfTurn;
    }
}
