package chess.entity;

public class ChessGameEntityBuilder {

    private long id;
    private String name;
    private String password;
    private boolean power;
    private String teamValueOfTurn;

    public ChessGameEntityBuilder setId(final long id) {
        this.id = id;
        return this;
    }

    public ChessGameEntityBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public ChessGameEntityBuilder setPassword(final String password) {
        this.password = password;
        return this;
    }

    public ChessGameEntityBuilder setPower(final boolean power) {
        this.power = power;
        return this;
    }

    public ChessGameEntityBuilder setTeamValueOfTurn(final String teamValueOfTurn) {
        this.teamValueOfTurn = teamValueOfTurn;
        return this;
    }

    public ChessGameEntity build() {
        return new ChessGameEntity(id, name, password, power, teamValueOfTurn);
    }
}
