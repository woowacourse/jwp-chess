package wooteco.chess.dto;

import java.util.HashMap;
import java.util.Map;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Pieces;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

public class ResultDto {
    private Team winner;
    private long roomId;
    private Map<String, Piece> pieceMap;

    private ResultDto(Team winner, long roomId, Map<String, Piece> pieceMap) {
        this.winner = winner;
        this.roomId = roomId;
        this.pieceMap = pieceMap;
    }

    public static ResultDto of(Board board, long roomId) {
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

    public long getRoomId() {
        return roomId;
    }

    public Map<String, Piece> getPieceMap() {
        return pieceMap;
    }
}
