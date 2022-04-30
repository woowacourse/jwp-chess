package chess.model;

import chess.model.board.Board;

public class GameResult {
    private final double whiteScore;
    private final double blackScore;
    private final Team winningTeam;

    private GameResult(double whiteScore, double blackScore, Team winningTeam) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.winningTeam = winningTeam;
    }

    public static GameResult from(Board board) {
        double whiteScore = board.getTotalScore(Team.WHITE);
        double blackScore = board.getTotalScore(Team.BLACK);
        if (board.countKing() == 1) {
            return getGameResultWhenKingDead(board, whiteScore, blackScore);
        }
        return new GameResult(whiteScore, blackScore, findWinningTeam(whiteScore, blackScore));
    }

    private static GameResult getGameResultWhenKingDead(Board board, double whiteScore, double blackScore) {
        long whiteKingCount = board.countKing(Team.WHITE);
        long blackKingCount = board.countKing(Team.BLACK);
        return new GameResult(whiteScore, blackScore, findWinningTeam(whiteKingCount, blackKingCount));
    }

    private static Team findWinningTeam(double white, double black) {
        if (white > black) {
            return Team.WHITE;
        }
        if (black > white) {
            return Team.BLACK;
        }
        return Team.NONE;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }
}
