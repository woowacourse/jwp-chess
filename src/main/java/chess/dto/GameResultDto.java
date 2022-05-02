package chess.dto;

import chess.domain.ChessGame;
import chess.domain.Result;

public class GameResultDto {

    private final String result;
    private final String enemyName;
    private final String team;
    private final double myScore;
    private final double enemyScore;

    public GameResultDto(String result, String enemyName, String team, double myScore, double enemyScore) {
        this.result = result;
        this.enemyName = enemyName;
        this.team = team;
        this.myScore = myScore;
        this.enemyScore = enemyScore;
    }

    public GameResultDto from(final ChessGame game, final Long memberId) {
        final String winResult = findWinResult(game, memberId);
        final String enemyName = findEnemyName(game, memberId);
        final String team = findTeam(game, memberId);
        final double myScore = findMyScore(game, memberId);
        final double enemyScore = findEnemyScore(game, memberId);

        return new GameResultDto(winResult, enemyName, team, myScore, enemyScore);
    }

    private static String findWinResult(final ChessGame game, final Long memberId) {
        final Long winnerId = game.getWinnerId();

        if (winnerId.equals(memberId)) {
            return "승";
        }
        return "패";
    }

    private static String findEnemyName(final ChessGame game, final Long memberId) {
        if (game.getParticipant().getBlackId().equals(memberId)) {
            return game.getParticipant().getWhiteName();
        }
        return game.getParticipant().getBlackName();
    }

    private static String findTeam(final ChessGame game, final Long memberId) {
        if (game.getParticipant().getBlackId().equals(memberId)) {
            return "흑";
        }
        return "백";
    }

    private static double findMyScore(final ChessGame game, final Long memberId) {
        final Result result = game.createResult();

        if (game.getParticipant().getBlackId().equals(memberId)) {
            return result.getBlackScore();
        }
        return result.getWhiteScore();
    }

    private static double findEnemyScore(final ChessGame game, final Long memberId) {
        final Result result = game.createResult();

        if (game.getParticipant().getBlackId().equals(memberId)) {
            return result.getWhiteScore();
        }
        return result.getBlackScore();
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
