package chess.webdto;

import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.Piece;

import java.util.HashMap;
import java.util.Map;

import static chess.service.TeamFormat.BLACK_TEAM;
import static chess.service.TeamFormat.WHITE_TEAM;

public class ChessGameDto {
    private final Map<String, Map<String, String>> piecePositionByTeam;
    private final String currentTurnTeam;
    private final Map<String, Double> teamScore;
    private final boolean isPlaying;
    private final int roomNumber;

    public static ChessGameDto of(final int gameId, final String currentTurnTeam, final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePositionAsString = generatePiecePositionAsString(chessGame);

        final Map<String, Double> teamScore = new HashMap<>();
        teamScore.put(WHITE_TEAM.asDtoFormat(), chessGame.calculateWhiteTeamScore());
        teamScore.put(BLACK_TEAM.asDtoFormat(), chessGame.calculateBlackTeamScore());

        return new ChessGameDto(piecePositionAsString, currentTurnTeam, teamScore, chessGame.isPlaying(), gameId);
    }

    private static Map<String, Map<String, String>> generatePiecePositionAsString(final ChessGame chessGame) {
        final Map<String, Map<String, String>> piecePosition = new HashMap<>();
        piecePosition.put(WHITE_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(chessGame.currentWhitePiecePosition()));
        piecePosition.put(BLACK_TEAM.asDtoFormat(), generateTeamPiecePositionAsString(chessGame.currentBlackPiecePosition()));
        return piecePosition;
    }

    private static Map<String, String> generateTeamPiecePositionAsString(final Map<Position, Piece> piecePosition) {
        final Map<String, String> piecePositionConverted = new HashMap<>();
        piecePosition.forEach((position, chosenPiece) -> {
            final String positionInitial = position.getPositionInitial();
            final String pieceAsString = PieceDtoFormat.convert(chosenPiece);
            piecePositionConverted.put(positionInitial, pieceAsString);
        });
        return piecePositionConverted;
    }

    private ChessGameDto(final Map<String, Map<String, String>> piecePositionByTeam, final String currentTurnTeam,
                        final Map<String, Double> teamScore, final boolean isPlaying, final int roomNumber) {
        this.piecePositionByTeam = piecePositionByTeam;
        this.currentTurnTeam = currentTurnTeam;
        this.teamScore = teamScore;
        this.isPlaying = isPlaying;
        this.roomNumber = roomNumber;
    }

    public Map<String, Map<String, String>> getPiecePositionByTeam() {
        return piecePositionByTeam;
    }

    public String getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    public Map<String, Double> getTeamScore() {
        return teamScore;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
