package chess.domain.board.dto;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.Score;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class BoardDto {

    private Map<String, Object> board = new HashMap<>();
    private boolean gameSet;
    private String currentTurn;
    private Map<String, Object> gameResult;

    public BoardDto(ChessGame chessGame) {
        Board board = chessGame.board();
        Map<Position, Piece> pieceMap = board.asMap();

        for (Position position : pieceMap.keySet()) {
            Piece piece = pieceMap.get(position);
            String initial = convertToInitial(piece);

            this.board.put(position.toString(), initial);
        }

        if (chessGame.isGameSet()) {
            this.gameResult = result(chessGame);
        }

        this.gameSet = chessGame.isGameSet();
        this.currentTurn = chessGame.currentTurn().toString();
    }

    private String convertToInitial(Piece piece) {
        String initial = piece.getInitial();
        if (".".equals(initial)) {
            initial = "";
        }
        return initial;
    }

    private Map<String, Object> result(ChessGame chessGame) {
        Map<String, Object> result = new HashMap<>();

        result.put("winner", chessGame.winner().toString());

        Score score = chessGame.score();
        result.put("blackScore", score.blackScore());
        result.put("whiteScore", score.whiteScore());

        return result;
    }

    public Map<String, Object> getBoard() {
        return board;
    }

    public boolean isGameSet() {
        return gameSet;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }

    public Map<String, Object> getGameResult() {
        return gameResult;
    }
}
