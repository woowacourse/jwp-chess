package wooteco.chess.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

public class ResultDto {
    private Team winner;
    private UUID roomId;
    private Map<String, Piece> pieceMap;

    private ResultDto(Team winner, UUID roomId, Map<String, Piece> pieceMap) {
        this.winner = winner;
        this.roomId = roomId;
        this.pieceMap = pieceMap;
    }

    public static ResultDto of(Board board, UUID roomId) {
        Map<String, Piece> pieceMap = new HashMap<>();
        Pieces pieces = board.getPieces();
        for (Piece alivePiece : pieces.getAlivePieces()) {
            pieceMap.put(alivePiece.getPosition(), alivePiece);
        }
        return new ResultDto(board.getWinner(), roomId, pieceMap);
    }

    public Team getWinner() {
        return winner;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public Map<String, Piece> getPieceMap() {
        return pieceMap;
    }
}
