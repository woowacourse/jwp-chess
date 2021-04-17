package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;

import java.util.Map;
import java.util.stream.Collectors;

public class RoomDTO {
    private final int roomNo;
    private final String roomName;
    private final String turn;
    private final String board;

    public RoomDTO(int roomNo, String roomName, String turn, String board) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.turn = turn;
        this.board = board;
    }

    public RoomDTO(int roomNo, String roomName, ChessGame chessGame) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.turn = chessGame.getTurn().getColor();
        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        this.board = chessBoard.keySet().stream()
                .map(position -> position.getPosition() + " " + chessBoard.get(position).getType() + " " + chessBoard.get(position).getColor())
                .collect(Collectors.joining(","));
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTurn() {
        return turn;
    }

    public String getBoard() {
        return board;
    }
}
