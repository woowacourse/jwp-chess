package chess.dto;

import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.model.square.Square;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreResult {

    private static final int PAWN_SAME_LINE_COUNT = 2;
    private static final double PAWN_SAME_LINE_POINT = 0.5;

    private final Map<Team, Double> scoreResult;

    private ScoreResult(Map<Team, Double> scoreResult) {
        this.scoreResult = scoreResult;
    }

    public static ScoreResult from(Map<Square, Piece> board) {
        Map<Team, Double> scoreResult = Team.getPlayerTeams().stream()
                .collect(Collectors.toMap(team -> team, team -> sumMajorPiecesPoint(board, team)));
        scoreResult.replaceAll((team, v) -> scoreResult.get(team) + pawnScore(collectPawns(board, team)));
        return new ScoreResult(scoreResult);
    }

    public Double get(Team team) {
        return scoreResult.get(team);
    }

    private static double pawnScore(Map<Square, Piece> whitePawns) {
        return Arrays.stream(File.values())
                .mapToInt(file -> file.countPawnsInSameFile(whitePawns.keySet()))
                .mapToDouble(ScoreResult::calculatePawnPoint)
                .sum();
    }

    private static Map<Square, Piece> collectPawns(Map<Square, Piece> board, Team team) {
        return board.keySet().stream()
                .filter(square -> board.get(square).isPawn() && board.get(square).isSameTeam(team))
                .collect(Collectors.toMap(square -> square, board::get));
    }

    private static double sumMajorPiecesPoint(Map<Square, Piece> board, Team team) {
        return board.values().stream()
                .filter(piece -> !piece.isPawn() && piece.isSameTeam(team))
                .mapToDouble(Piece::getPoint)
                .sum();
    }

    private static double calculatePawnPoint(int count) {
        if (count >= PAWN_SAME_LINE_COUNT) {
            return count * PAWN_SAME_LINE_POINT;
        }
        return count;
    }
}
