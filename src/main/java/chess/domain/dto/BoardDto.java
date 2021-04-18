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

    private BoardDto(Map<String, String> boardInfo, Boolean gameOverFlag) {
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
        Boolean gameOverFlag = checkGameOverFlag(board);
        return new BoardDto(boardInfo, gameOverFlag);
    }

    private static Boolean checkGameOverFlag(Board board) {
        return board.isAnyKingDead();
    }

    public static BoardDto of(Map<String, String> boardInfo) {
        return new BoardDto(boardInfo, true);
    }

    public boolean getGameOverFlag() {
        return gameOverFlag;
    }

    public Map<String, String> getBoardInfo() {
        return boardInfo;
    }
}
