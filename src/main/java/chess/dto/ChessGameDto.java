package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;

import java.util.Map;
import java.util.stream.Collectors;

public class ChessGameDto {
    private final int roomNo;
    private final String roomName;
    private final String turn;
    private final Map<String, PieceDto> chessBoard;
    private final ScoreDto scoreDto;

    public ChessGameDto(int roomNo, String roomName, ChessGame chessGame) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.turn = chessGame.getTurn().getColor();
        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        this.chessBoard = chessBoard.keySet().stream()
                .collect(Collectors.toMap(position -> position.getPosition(), position -> new PieceDto(chessBoard.get(position))));
        this.scoreDto = new ScoreDto(chessGame.calculateResult());
    }

    public String getTurn() {
        return turn;
    }

    public Map<String, PieceDto> getChessBoard() {
        return chessBoard;
    }

    public ScoreDto getScoreDto() {
        return scoreDto;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getRoomName() {
        return roomName;
    }
}
