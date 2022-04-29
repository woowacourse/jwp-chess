package chess.dto;

import chess.domain.ChessGame;
import java.util.Map;

public class GameStatusDto {

    private String turn;
    private Map<String, String> board;
    private String gameStatus;

    public GameStatusDto() {
    }

    public GameStatusDto(String turn, Map<String, String> board, String gameStatus) {
        this.turn = turn;
        this.board = board;
        this.gameStatus = gameStatus;
    }

    public static GameStatusDto of(ChessGame chessGame) {
        return new GameStatusDto(chessGame.getTurn().toString(),
                chessGame.toMap(), chessGame.getGameStatus().toString());
    }

    public String getTurn() {
        return turn;
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public String getGameStatus() {
        return gameStatus;
    }
}
