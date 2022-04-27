package chess.dto;

public class GameResultDto {

    private final Long gameId;
    private final String result;
    private final String enemyName;
    private final String team;
    private final double myScore;
    private final double enemyScore;

    public GameResultDto(final Long gameId,
                         final String result,
                         final String enemyName,
                         final String team,
                         final double myScore,
                         final double enemyScore) {
        this.gameId = gameId;
        this.result = result;
        this.enemyName = enemyName;
        this.team = team;
        this.myScore = myScore;
        this.enemyScore = enemyScore;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getResult() {
        return result;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public String getTeam() {
        return team;
    }

    public double getMyScore() {
        return myScore;
    }

    public double getEnemyScore() {
        return enemyScore;
    }
}
