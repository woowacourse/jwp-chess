package chess.domain.dto;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.piece.Piece;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardDto {
    public boolean gameOverFlag = false;
    private Map<String, String> boardInfo = new HashMap<>();

    public BoardDto() {
    }

    private BoardDto(Map<String, String> boardInfo, boolean gameOverFlag) {
        this.boardInfo = boardInfo;
        this.gameOverFlag = gameOverFlag;
    }

    public static BoardDto of(Board board) {
        Map<String, String> boardInfo = new HashMap<>();
        for (Map.Entry<Position, Piece> info : board.getBoard().entrySet()) {
            if (Objects.equals(info.getValue(), null)) {
                boardInfo.put(info.getKey().convertToString(), "");
            } else {
                boardInfo.put(info.getKey().convertToString(), info.getValue().getUnicode());
            }
        }
        boolean gameOverFlag = getGameOverFlag(board);
        return new BoardDto(boardInfo, gameOverFlag);
    }

    private static boolean getGameOverFlag(Board board) {
        if (board.isAnyKingDead()) {
            return true;
        }
        return false;
    }

    public static BoardDto of(Map<String, String> boardInfo) {
        return new BoardDto(boardInfo, true);
    }

    public Map<String, String> getBoardInfo() {
        return boardInfo;
    }
}
