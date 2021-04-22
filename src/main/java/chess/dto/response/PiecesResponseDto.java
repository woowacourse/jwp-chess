package chess.dto.response;

import chess.domain.chessgame.ChessGame;
import chess.domain.chessgame.Room;
import chess.domain.piece.Color;
import java.util.ArrayList;
import java.util.List;


public class PiecesResponseDto {

    private final Color winnerColor;
    private final boolean isPlaying;
    private final List<PieceResponseDto> alivePieces;

    public PiecesResponseDto(ChessGame chessGame) {
        this.winnerColor = chessGame.winnerColor();
        this.isPlaying = chessGame.isPlaying();
        alivePieces = new ArrayList<>();
        chessGame.pieces().forEach((key, value) -> {
            alivePieces.add(new PieceResponseDto(key, value));
        });
    }

    public PiecesResponseDto(Room room) {
        this(room.chessgame());
    }

    public Color getWinnerColor() {
        return winnerColor;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public List<PieceResponseDto> getAlivePieces() {
        return alivePieces;
    }

}
