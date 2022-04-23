package chess.dto;

import chess.chessgame.ChessGame;
import chess.chessgame.Position;
import chess.piece.Piece;
import chess.state.State;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessGameDto {
    private final List<PieceDto> pieces;
    private final GameInfoDto gameInfo;

    public ChessGameDto(List<PieceDto> pieces, GameInfoDto gameInfo) {
        this.pieces = pieces;
        this.gameInfo = gameInfo;
    }

    private ChessGameDto(List<PieceDto> pieces, String state, String turn) {
        this.pieces = pieces;
        this.gameInfo = new GameInfoDto(state,turn);
    }

    public static ChessGameDto of(ChessGame chessGame) {
        Map<Position, Piece> chessboard = chessGame.getChessBoard();

        List<PieceDto> pieces = chessboard.keySet()
                .stream()
                .map(position -> new PieceDto(chessboard.get(position), position))
                .collect(Collectors.toList());

        String state = convertState(chessGame.getState());
        String turn = chessGame.getColorOfTurn();

        return new ChessGameDto(pieces, state, turn);
    }

    public List<PieceDto> getPieces() {
        return pieces;
    }

    public GameInfoDto getGameInfo() {
        return gameInfo;
    }

    public String getState() {
        return gameInfo.getState();
    }

    public String getTurn() {
        return gameInfo.getTurn();
    }

    private static String convertState(State state) {
        if (state.isPlaying()) {
            return "Play";
        }
        if (state.isFinished()) {
            return "Finish";
        }
        return "Ready";
    }
}
