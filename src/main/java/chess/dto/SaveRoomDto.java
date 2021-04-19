package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;

import java.util.Map;
import java.util.stream.Collectors;

public class SaveRoomDto {
    private final String roomName;
    private final String turn;
    private final String chessBoard;

    public SaveRoomDto(String roomName, ChessGame chessGame) {
        this.roomName = roomName;
        this.turn = chessGame.getTurn().getColor();
        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        this.chessBoard = chessBoard.keySet().stream()
                .map(position -> position.getPosition() + " " + chessBoard.get(position).getType() + " " + chessBoard.get(position).getColor())
                .collect(Collectors.joining(","));
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTurn() {
        return turn;
    }

    public String getChessBoard() {
        return chessBoard;
    }
}
