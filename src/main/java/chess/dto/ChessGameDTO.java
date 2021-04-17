package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Piece;

import java.util.Map;
import java.util.stream.Collectors;

public class ChessGameDTO {
    private final int roomNo;
    private final String roomName;
    private final String turn;
    private final Map<String, PieceDTO> chessBoard;
    private final ScoreDTO scoreDTO;

    public ChessGameDTO(int roomNo, String roomName, ChessGame chessGame) {
        this.roomNo = roomNo;
        this.roomName = roomName;
        this.turn = chessGame.getTurn().getColor();
        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        this.chessBoard = chessBoard.keySet().stream()
                .collect(Collectors.toMap(position -> position.getPosition(), position -> new PieceDTO(chessBoard.get(position))));
        this.scoreDTO = new ScoreDTO(chessGame.calculateResult());
    }

    public String getTurn() {
        return turn;
    }

    public Map<String, PieceDTO> getChessBoard() {
        return chessBoard;
    }

    public ScoreDTO getScoreDTO() {
        return scoreDTO;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public String getRoomName() {
        return roomName;
    }
}
