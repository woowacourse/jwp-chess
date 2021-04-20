package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class RoomDto {
    private final int roomNo;
    private final String roomName;
    private final String turn;
    private final String board;

    public RoomDto(int roomNo, String roomName, String turn, String board) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.turn = turn;
        this.board = board;
    }

    public RoomDto(int roomNo, String roomName, ChessGame chessGame) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return roomNo == roomDto.roomNo && Objects.equals(roomName, roomDto.roomName) && Objects.equals(turn, roomDto.turn) && Objects.equals(board, roomDto.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNo, roomName, turn, board);
    }

    @Override
    public String toString() {
        return "RoomDto{" +
                "roomNo=" + roomNo +
                ", roomName='" + roomName + '\'' +
                ", turn='" + turn + '\'' +
                ", board='" + board + '\'' +
                '}';
    }
}
