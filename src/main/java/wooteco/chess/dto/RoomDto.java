package wooteco.chess.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

public class RoomDto {
    private Map<String, Piece> pieceMap;
    private double teamWhiteScore;
    private double teamBlackScore;
    private UUID id;
    private String name;
    private Team turn;

    private RoomDto(Map<String, Piece> pieceMap, double teamWhiteScore, double teamBlackScore, UUID id, String name,
        Team turn) {
        this.pieceMap = pieceMap;
        this.teamWhiteScore = teamWhiteScore;
        this.teamBlackScore = teamBlackScore;
        this.id = id;
        this.name = name;
        this.turn = turn;
    }

    public static RoomDto of(Board board, UUID id, String name) {
        Map<String, Piece> pieceMap = new HashMap<>();
        Pieces pieces = board.getPieces();
        for (Piece alivePiece : pieces.getAlivePieces()) {
            pieceMap.put(alivePiece.getPosition(), alivePiece);
        }
        return new RoomDto(pieceMap, board.calculateScoreByTeam(Team.WHITE), board.calculateScoreByTeam(Team.BLACK), id,
            name, board.getTurn());
    }

    public Map<String, Piece> getPieceMap() {
        return pieceMap;
    }

    public double getTeamWhiteScore() {
        return teamWhiteScore;
    }

    public double getTeamBlackScore() {
        return teamBlackScore;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Team getTurn() {
        return turn;
    }
}
